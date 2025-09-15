package com.volunteer.controller;

import com.volunteer.dto.event.*;
import com.volunteer.dto.organization.DetailResponse;
import com.volunteer.entity.VolunteerDonation;
import com.volunteer.repository.CharityEventRepository;
import com.volunteer.repository.DonationEventRepository;
import com.volunteer.repository.VolunteerCharityEventRepository;
import com.volunteer.repository.VolunteerDonationRepository;
import com.volunteer.service.CharityEventService;
import com.volunteer.service.DonationEventService;
import com.volunteer.service.OrganizationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private CharityEventService charityEventService;
    @Autowired
    private DonationEventService donationEventService;
    @Autowired
    private CharityEventRepository charityEventRepository;
    @Autowired
    private DonationEventRepository donationEventRepository;
    @Autowired
    private VolunteerDonationRepository volunteerDonationRepository;
    @Autowired
    private VolunteerCharityEventRepository volunteerCharityEventRepository;
    @Autowired
    private OrganizationService organizationService;

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

    @PostMapping("/charity/{eventId}/checkin")
    public ResponseEntity<String> checkinVolunteer(
            @PathVariable("eventId") Long eventId,
            @RequestParam(value = "volunteerId", required = true) Long volunteerId
    ) {
        charityEventService.checkinVolunteer(eventId, volunteerId);
        return ResponseEntity.ok("Check-in thành công!");
    }

    @GetMapping("/charity/{eventId}/users")
    public ResponseEntity<List<VolunteerEventParticipationResponse>> getAllCharities(
            @PathVariable("eventId") Long volunteerId // để check joined
    ) {
        List<VolunteerEventParticipationResponse> result = charityEventService.getVolunteersByCharity(volunteerId);
        return ResponseEntity.ok(result);
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
    @PostMapping("/donation")
    public ResponseEntity<DonationEventResponse> createDonationEvent(
            @ModelAttribute @Valid DonationEventRequest request) throws IOException {
        logger.info("Create donation event endpoint called for title: {}", request.getTitle());
        DonationEventResponse response = donationEventService.createDonationEvent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/donation")
    public ResponseEntity<List<DonationEventResponse>> getListDonation(
    ) {
        return ResponseEntity.ok(donationEventService.getListDonation());
    }

    @GetMapping("/donation/{organizationId}/organization")
    public ResponseEntity<List<DonationEventResponse>> getDonationsByOrganization(
            @PathVariable("organizationId") Long organizationId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(
                donationEventService.getDonationsByOrganization(organizationId, title, from, to)
        );
    }

    @GetMapping("/donation/{id}")
    public ResponseEntity<DonationEventResponse> getDonationEventById(@PathVariable("id") Long id) {
        logger.info("Get donation event by id endpoint called for id: {}", id);
        DonationEventResponse response = donationEventService.getDonationEventById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/donation/{eventId}/users")
    public ResponseEntity<List<VolunteerDonationResponse>> getAllDonatorsByEvent(
            @PathVariable("eventId") Long eventId
    ) {
        List<VolunteerDonationResponse> result = donationEventService.getVolunteersByDonationEvent(eventId);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/donation/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DonationEventResponse> updateDonationEvent(
            @PathVariable Long id,
            @Valid @ModelAttribute DonationEventRequest request) throws IOException {

        logger.info("PUT /donation/{} called with multipart form-data", id);
        DonationEventResponse response = donationEventService.updateDonationEvent(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/donation/{id}")
    public ResponseEntity<Void> deleteDonationEvent(@PathVariable Long id) {
        logger.info("Delete donation event endpoint called for id: {}", id);
        donationEventService.deleteDonationEvent(id);
        return ResponseEntity.noContent().build();
    }

    // General event listing for users
    @GetMapping("/dashboard/organization/{organizationId}")
    public ResponseEntity<Map<String, Object>> getAllEventsByOrganization(
            @PathVariable("organizationId") Long organizationId) {
        // đếm số charity events theo org
        long charityCount = charityEventRepository.countByOrganizationId(organizationId);

        // đếm số donation events theo org
        long donationCount = donationEventRepository.countByOrganizationId(organizationId);

        Map<String, Object> result = new HashMap<>();
        result.put("organizationId", organizationId);
        result.put("charityCount", charityCount);
        result.put("donationCount", donationCount);
        result.put("total", charityCount + donationCount);

        return ResponseEntity.ok(result);
    }

    // Get event details by eventId
    @GetMapping("/dashboard/user")
    public ResponseEntity<Map<String, Object>> getUserDashboard(
            @RequestParam("userId") Long userId,
            @RequestParam("organizationId") Long organizationId) {

        Map<String, Object> result = new HashMap<>();

        // 1. Tổng tiền donate
        BigDecimal totalDonated = volunteerDonationRepository
                .findByVolunteerId(userId)
                .stream()
                .map(VolunteerDonation::getDonateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalDonated", totalDonated);

        // 2. Số event đã tham gia (charity)
        long charityJoinedCount = volunteerCharityEventRepository
                .countByVolunteerId(userId);
        result.put("charityJoinedCount", charityJoinedCount);

        // 3. Số charity mà user tạo (organizationId)
        long charityCreatedCount = charityEventRepository
                .countByOrganizationId(organizationId);
        result.put("charityCreatedCount", charityCreatedCount);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/organization/{id}")
    public ResponseEntity<DetailResponse> getOrganizationDetail(
            @PathVariable("id") Long organizationId,
            @RequestParam(value = "volunteerId", required = false) Long volunteerId
    ) {
        DetailResponse result = organizationService.getOrganizationDetailAndCharites(organizationId, volunteerId);
        return ResponseEntity.ok(result);
    }
} 