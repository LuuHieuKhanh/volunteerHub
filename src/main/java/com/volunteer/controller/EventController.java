package com.volunteer.controller;

import com.volunteer.dto.event.CharityEventRequest;
import com.volunteer.dto.event.CharityEventResponse;
import com.volunteer.dto.event.DonationEventRequest;
import com.volunteer.dto.event.DonationEventResponse;
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

    // Charity Event Endpoints
    @PostMapping("/charity")
    public ResponseEntity<CharityEventResponse> createCharityEvent(@ModelAttribute @Valid CharityEventRequest request) throws IOException {
        logger.info("Create charity event endpoint called for name: {}", request.getCharityName());
        CharityEventResponse response = charityEventService.createCharityEvent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/charity/{organizationId}/organization")
    public ResponseEntity<List<CharityEventResponse>> getCharitiesByOrganization(
            @PathVariable("organizationId") Long organizationId) {
        return ResponseEntity.ok(charityEventService.getCharitiesByOrganization(organizationId));
    }

    @GetMapping("/charity/{id}")
    public ResponseEntity<CharityEventResponse> getCharityEventById(@PathVariable Long id) {
        logger.info("Get charity event by id endpoint called for id: {}", id);
        CharityEventResponse response = charityEventService.getCharityEventById(id);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<DonationEventResponse> createDonationEvent(@Valid @RequestBody DonationEventRequest request) {
        logger.info("Create donation event endpoint called for title: {}", request.getTitle());
        DonationEventResponse response = donationEventService.createDonationEvent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/donation/{id}")
    public ResponseEntity<DonationEventResponse> getDonationEventById(@PathVariable Long id) {
        logger.info("Get donation event by id endpoint called for id: {}", id);
        DonationEventResponse response = donationEventService.getDonationEventById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/donation/{id}")
    public ResponseEntity<DonationEventResponse> updateDonationEvent(@PathVariable Long id, @Valid @RequestBody DonationEventRequest request) {
        logger.info("Update donation event endpoint called for id: {}", id);
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