package com.volunteer.controller;

import com.volunteer.dto.event.*;
import com.volunteer.service.CharityEventService;
import com.volunteer.service.DonationEventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private CharityEventService charityEventService;
    @Autowired
    private DonationEventService donationEventService;

    @GetMapping("/charity")
    public ResponseEntity<List<CharityEventResponseList>> searchCharitiesByOrganization(
            @RequestParam(value = "id", required = false) Long volunteerId // truyền volunteerId để check joined
    ) {
        List<CharityEventResponseList> result = charityEventService.getAllCharities(volunteerId);
        return ResponseEntity.ok(result);
    }

    // Charity Event Endpoints
    @PostMapping("/charity")
    public ResponseEntity<CharityEventResponse> createCharityEvent(@ModelAttribute @Valid CharityEventRequest request) throws IOException {
        logger.info("Create charity event endpoint called for name: {}", request.getCharityName());
        CharityEventResponse response = charityEventService.createCharityEvent(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/charity/{eventId}/join")
    public ResponseEntity<String> joinProgram(
            @PathVariable("eventId") Long eventId,
            @RequestParam(value = "volunteerId") Long volunteerId // nếu có JWT thì lấy từ token thay vì request param
    ) {
        charityEventService.joinProgram(eventId, volunteerId);
        return ResponseEntity.ok("Joined successfully");
    }

    @PostMapping("/charity/{eventId}/leave")
    public ResponseEntity<String> leaveProgram(
            @PathVariable("eventId") Long eventId,
            @RequestParam(value = "volunteerId") Long volunteerId // nếu có JWT thì lấy từ token thay vì request param
    ) {
        charityEventService.leaveProgram(eventId, volunteerId);
        return ResponseEntity.ok("Leaved successfully");
    }

    @GetMapping("/charity/{organizationId}/organization")
    public ResponseEntity<List<CharityEventResponse>> getCharitiesByOrganization(
            @PathVariable("organizationId") Long organizationId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(
                charityEventService.getCharitiesByOrganization(organizationId, name, from, to)
        );
    }

    @GetMapping("/charity/{id}")
    public ResponseEntity<CharityEventResponseList> getCharityById(
            @PathVariable("id") Long charityId,
            @RequestParam(value = "volunteerId", required = false) Long volunteerId
    ) {
        CharityEventResponseList result = charityEventService.getCharityById(charityId, volunteerId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/charity/{id}")
    public ResponseEntity<CharityEventResponse> updateCharityEvent(@PathVariable Long id, @Valid @RequestBody CharityEventRequest request) throws IOException {
        logger.info("Update charity event endpoint called for id: {}", id);
        CharityEventResponse response = charityEventService.updateCharityEvent(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/charity/{id}")
    public ResponseEntity<Void> deleteCharityEvent(@PathVariable Long id) {
        logger.info("Delete charity event endpoint called for id: {}", id);
        charityEventService.deleteCharityEvent(id);
        return ResponseEntity.noContent().build();
    }

    // Donation Event Endpoints
    @GetMapping("/donation")
    public ResponseEntity<List<com.volunteer.dto.donation.DonationEventListResponse>> getAllDonationEvents(
            @RequestParam(value = "search", required = false) String search) {
        logger.info("Get all donation events endpoint called with search: {}", search);
        List<com.volunteer.dto.donation.DonationEventListResponse> response = donationEventService.getAllDonationEvents(search);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/donation")
    public ResponseEntity<com.volunteer.dto.donation.DonationEventListResponse> createDonationEvent(@Valid @RequestBody com.volunteer.dto.donation.DonationEventCreateRequest request) {
        logger.info("Create donation event endpoint called for title: {}", request.getTitle());
        com.volunteer.dto.donation.DonationEventListResponse response = donationEventService.createDonationEvent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/donation/{id}")
    public ResponseEntity<com.volunteer.dto.donation.DonationEventDetailResponse> getDonationEventById(@PathVariable Long id) {
        logger.info("Get donation event by id endpoint called for id: {}", id);
        com.volunteer.dto.donation.DonationEventDetailResponse response = donationEventService.getDonationEventDetail(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/donation/{id}")
    public ResponseEntity<com.volunteer.dto.donation.DonationEventListResponse> updateDonationEvent(@PathVariable Long id, @Valid @RequestBody com.volunteer.dto.donation.DonationEventUpdateRequest request) {
        logger.info("Update donation event endpoint called for id: {}", id);
        com.volunteer.dto.donation.DonationEventListResponse response = donationEventService.updateDonationEvent(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/donation/{id}")
    public ResponseEntity<Void> deleteDonationEvent(@PathVariable Long id) {
        logger.info("Delete donation event endpoint called for id: {}", id);
        donationEventService.softDeleteDonationEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/donation/volunteer/{volunteerId}/history")
    public ResponseEntity<List<com.volunteer.dto.donation.VolunteerDonationHistoryResponse>> getVolunteerDonationHistory(@PathVariable("volunteerId") Long volunteerId) {
        logger.info("Get volunteer donation history endpoint called for volunteer id: {}", volunteerId);
        List<com.volunteer.dto.donation.VolunteerDonationHistoryResponse> response = donationEventService.getVolunteerDonationHistory(volunteerId);
        return ResponseEntity.ok(response);
    }

    // General event listing for users
    @GetMapping("")
    public ResponseEntity<List<?>> getAllEvents() {
        // TODO: Return all events (charity + donation)
        return ResponseEntity.ok(List.of());
    }

    // Get event details by eventId
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventDetails(@PathVariable Long eventId) {
        // TODO: Return event details
        return ResponseEntity.ok("Event details for event " + eventId);
    }
}
