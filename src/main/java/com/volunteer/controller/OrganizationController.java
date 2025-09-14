package com.volunteer.controller;

import com.volunteer.dto.organization.OrganizationRequest;
import com.volunteer.dto.organization.OrganizationResponse;
import com.volunteer.dto.organization.OrganizationCreateRequest;
import com.volunteer.dto.organization.OrganizationListResponse;
import com.volunteer.entity.Organization;
import com.volunteer.service.OrganizationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationListResponse> createOrganization(@Valid @RequestBody OrganizationCreateRequest request) {
        logger.info("Create organization endpoint called for name: {}", request.getOrganizationName());

        // Check if current user is admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        OrganizationListResponse response = organizationService.createOrganizationByAdmin(request, isAdmin);
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
    public ResponseEntity<List<Object[]>> getOrganizationEvents(@PathVariable Long orgId) {
        List<Object[]> events = List.of(
                new Object[]{1L, "Book Fair", "Hue", "2025-09-01", "2025-09-03", "Upcoming"},
                new Object[]{2L, "Charity Run", "Da Lat", "2025-07-15", "2025-07-16", "Ongoing"}
        );

        return ResponseEntity.ok(events);
    }

    // View status of a specific event created by this organization
    @GetMapping("/{orgId}/events/{eventId}")
    public ResponseEntity<?> getOrganizationEventStatus(@PathVariable Long orgId, @PathVariable Long eventId) {
        // TODO: Return event status/details
        return ResponseEntity.ok("Event status for event " + eventId);
    }
}
