package com.volunteer.controller;

import com.volunteer.dto.volunteer.VolunteerResponse;
import com.volunteer.entity.Volunteer;
import com.volunteer.service.VolunteerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerController.class);

    @Autowired
    private VolunteerService volunteerService;

    @PostMapping
    public ResponseEntity<VolunteerResponse> createVolunteer(@Valid @RequestBody Volunteer volunteer) {
        logger.info("Create volunteer endpoint called for username: {}", volunteer.getFullName());
        VolunteerResponse response = volunteerService.createVolunteer(volunteer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolunteerResponse> getVolunteerById(@PathVariable Long id) {
        logger.info("Get volunteer by id endpoint called for id: {}", id);
        VolunteerResponse response = volunteerService.getVolunteerById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VolunteerResponse> updateVolunteer(@PathVariable Long id, @Valid @RequestBody Volunteer volunteer) {
        logger.info("Update volunteer endpoint called for id: {}", id);
        VolunteerResponse response = volunteerService.updateVolunteer(id, volunteer);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> softDeleteCurrentUserAccount(@RequestParam Long id) {
        logger.info("Soft delete current user account endpoint called for id: {}", id);
        volunteerService.softDeleteCurrentUserAccount(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteVolunteerAccount(@PathVariable Long id) {
        logger.info("Hard delete volunteer account endpoint called for id: {}", id);
        volunteerService.hardDeleteVolunteerAccount(id);
        return ResponseEntity.noContent().build();
    }

    // Participate in a volunteer event
    @PostMapping("/events/{eventId}/participate")
    public ResponseEntity<?> participateInEvent(@PathVariable Long eventId) {
        // TODO: Implement participation logic
        return ResponseEntity.ok("Participated in event " + eventId);
    }

    // Cancel event participation
    @DeleteMapping("/events/{eventId}/participation")
    public ResponseEntity<?> cancelEventParticipation(@PathVariable Long eventId) {
        // TODO: Implement cancellation logic
        return ResponseEntity.ok("Cancelled participation in event " + eventId);
    }

    // View organizations
    @GetMapping("/organizations")
    public ResponseEntity<List<?>> viewOrganizations() {
        // TODO: Return list of organizations
        return ResponseEntity.ok(List.of());
    }

    // View events
    @GetMapping("/events")
    public ResponseEntity<List<?>> viewEvents() {
        // TODO: Return list of events
        return ResponseEntity.ok(List.of());
    }

    // View donation campaigns
    @GetMapping("/donations")
    public ResponseEntity<List<?>> viewDonations() {
        // TODO: Return list of donation campaigns
        return ResponseEntity.ok(List.of());
    }

    // Donate to a campaign
    @PostMapping("/donations/{donationId}/donate")
    public ResponseEntity<?> donateToCampaign(@PathVariable Long donationId, @RequestBody Object donationRequest) {
        // TODO: Implement donation logic
        return ResponseEntity.ok("Donated to campaign " + donationId);
    }

    // Request organization upgrade
    @PostMapping("/requests/organization-upgrade")
    public ResponseEntity<?> requestOrganizationUpgrade(@RequestBody Object upgradeRequest) {
        // TODO: Implement upgrade request logic
        return ResponseEntity.ok("Organization upgrade requested");
    }
} 