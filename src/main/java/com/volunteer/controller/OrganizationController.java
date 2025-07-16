package com.volunteer.controller;

import com.volunteer.dto.organization.OrganizationRequest;
import com.volunteer.dto.organization.OrganizationResponse;
import com.volunteer.service.OrganizationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(@Valid @RequestBody OrganizationRequest request) {
        logger.info("Create organization endpoint called for name: {}", request.getOrganizationName());
        OrganizationResponse response = organizationService.createOrganization(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable Long id) {
        logger.info("Get organization by id endpoint called for id: {}", id);
        OrganizationResponse response = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> updateOrganization(@PathVariable Long id, @Valid @RequestBody OrganizationRequest request) {
        logger.info("Update organization endpoint called for id: {}", id);
        OrganizationResponse response = organizationService.updateOrganization(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        logger.info("Delete organization endpoint called for id: {}", id);
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }

    // View all events created by this organization
    @GetMapping("/{orgId}/events")
    public ResponseEntity<List<?>> getOrganizationEvents(@PathVariable Long orgId) {
        // TODO: Return list of events for the organization
        return ResponseEntity.ok(List.of());
    }

    // View status of a specific event created by this organization
    @GetMapping("/{orgId}/events/{eventId}")
    public ResponseEntity<?> getOrganizationEventStatus(@PathVariable Long orgId, @PathVariable Long eventId) {
        // TODO: Return event status/details
        return ResponseEntity.ok("Event status for event " + eventId);
    }
} 