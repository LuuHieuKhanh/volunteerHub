package com.volunteer.controller;

import com.volunteer.dto.auth.MessageResponse;
import com.volunteer.dto.request.RequestResponse;
import com.volunteer.dto.volunteer.VolunteerResponse;
import com.volunteer.entity.Account;
import com.volunteer.entity.Volunteer;
import com.volunteer.entity.Request;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.AccountRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.RequestRepository;
import com.volunteer.repository.VolunteerRepository;
import com.volunteer.service.RequestService;
import com.volunteer.service.VolunteerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RequestService requestService;
    @Autowired
    private OrganizationRepository organizationRepository;

    // Get all volunteers (accounts)
    @GetMapping("/accounts")
    public ResponseEntity<List<VolunteerResponse>> getAllAccounts() {
        List<VolunteerResponse> volunteers = volunteerRepository.findAll()
                .stream()
                .map(volunteerService::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(volunteers);
    }

    // Activate/deactivate a volunteer account
    @PutMapping("/accounts/{id}/status")
    public ResponseEntity<MessageResponse> updateAccountStatus(@PathVariable Long id, @RequestParam boolean active) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        Account account = volunteer.getAccount();
        if (account == null) {
            throw new RuntimeException("Account not linked to this volunteer");
        }

        account.setActive(active);
        accountRepository.save(account);
        volunteerRepository.save(volunteer);
        return ResponseEntity.ok(new MessageResponse("Account status updated"));
    }

    // Activate/deactivate user account
    @PutMapping("/accounts/{userId}/status")
    public ResponseEntity<?> manageUserAccountStatus(@PathVariable Long userId, @RequestBody Object statusUpdateRequest) {
        // TODO: Implement user account status management
        return ResponseEntity.ok("User account " + userId + " status updated");
    }

    // Upgrade user account to Admin role
    @PutMapping("/accounts/{userId}/role")
    public ResponseEntity<?> upgradeUserToAdmin(@PathVariable Long userId, @RequestBody Object roleUpdateRequest) {
        // TODO: Implement role upgrade logic
        return ResponseEntity.ok("User " + userId + " upgraded to Admin role");
    }

    // Get all organization upgrade requests (filter by type if needed)
    @GetMapping("/organization-upgrade-requests")
    public ResponseEntity<List<RequestResponse>> getAllUpgradeRequests() {
        List<Request> requests = requestRepository.findAll(); // You may want to filter by type/status

        if (requests.isEmpty()) {
            throw new ResourceNotFoundException("NOT FOUND");
        }

        List<RequestResponse> response = requests.stream()
                .map(req -> new RequestResponse(
                        req.getId(),
                        req.getOrganization().getOrganizationName(),
                        req.getVolunteer().getFullName(),
                        req.getVolunteer().getAccount().getEmail(),
                        req.getVolunteer().getContact(),
                        req.getOrganization().getDescription(),
                        req.getDenyReason(),
                        req.getOrganization().getLogo(),
                        req.getOrganization().getCertificate(),
                        req.getRequestType(),
                        req.getVolunteer().getId(),
                        req.getStatus(),
                        req.getUpdatedAt()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Approve/reject an organization upgrade request
    @PostMapping("/organization-upgrade-requests/{requestId}/decision")
    public ResponseEntity<MessageResponse> decideUpgradeRequest(@PathVariable Long requestId, @RequestParam boolean approve, @RequestParam(required = false) String reason) {
        if (approve) {
            requestService.approveRequest(requestId);
            return ResponseEntity.ok(new MessageResponse("Request approved"));
        } else {
            requestService.rejectRequest(requestId, reason != null ? reason : "Rejected by admin");
            return ResponseEntity.ok(new MessageResponse("Request rejected"));
        }
    }

    // Approve/reject organization upgrade request
    @PutMapping("/organization-upgrade-requests/{requestId}/decision")
    public ResponseEntity<?> manageOrganizationUpgradeRequest(@PathVariable Long requestId, @RequestBody Object decisionRequest) {
        // TODO: Implement organization upgrade request decision logic
        return ResponseEntity.ok("Organization upgrade request " + requestId + " decision updated");
    }

    // Approve/reject organization
    @PutMapping("/organizations/{orgId}/status")
    public ResponseEntity<?> manageOrganizationStatus(@PathVariable Long orgId, @RequestBody Object statusUpdateRequest) {
        // TODO: Implement organization status management
        return ResponseEntity.ok("Organization " + orgId + " status updated");
    }

    // View all organizations
    @GetMapping("/organizations")
    public ResponseEntity<List<?>> getAllOrganizations() {
        // TODO: Return all organizations
        return ResponseEntity.ok(List.of());
    }

    // View all events (charity and donation)
    @GetMapping("/events")
    public ResponseEntity<List<?>> getAllEvents() {
        // TODO: Return all events
        return ResponseEntity.ok(List.of());
    }

    // Approve/reject event
    @PutMapping("/events/{eventId}/status")
    public ResponseEntity<?> manageEventStatus(@PathVariable Long eventId, @RequestBody Object statusUpdateRequest) {
        // TODO: Implement event status management
        return ResponseEntity.ok("Event " + eventId + " status updated");
    }

    // View all requests
    @GetMapping("/requests")
    public ResponseEntity<List<?>> getAllRequests() {
        // TODO: Return all requests
        return ResponseEntity.ok(List.of());
    }

    // Approve/reject request
    @PutMapping("/requests/{requestId}/decision")
    public ResponseEntity<?> manageRequestDecision(@PathVariable Long requestId, @RequestBody Object decisionRequest) {
        // TODO: Implement request decision logic
        return ResponseEntity.ok("Request " + requestId + " decision updated");
    }

    // View user profile
    @GetMapping("/accounts/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        // TODO: Return user profile
        return ResponseEntity.ok("User profile for user " + userId);
    }

    // Get system statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getSystemStats() {
        // TODO: Return system statistics
        return ResponseEntity.ok("System statistics");
    }

    // View all organization upgrade requests
//    @GetMapping("/organization-upgrade-requests")
//    public ResponseEntity<List<?>> getAllOrganizationUpgradeRequests() {
//        // TODO: Return all organization upgrade requests
//        return ResponseEntity.ok(List.of());
//    }

    // View all user accounts
//    @GetMapping("/accounts")
//    public ResponseEntity<List<?>> getAllUserAccounts() {
//        // TODO: Return all user accounts
//        return ResponseEntity.ok(List.of());
//    }

    // View all admin users
    @GetMapping("/admins")
    public ResponseEntity<List<?>> getAllAdmins() {
        // TODO: Return all admin users
        return ResponseEntity.ok(List.of());
    }

    // View all volunteers
    @GetMapping("/volunteers")
    public ResponseEntity<List<?>> getAllVolunteers() {
        // TODO: Return all volunteers
        return ResponseEntity.ok(List.of());
    }

    // View all donation campaigns
    @GetMapping("/donations")
    public ResponseEntity<List<?>> getAllDonations() {
        // TODO: Return all donation campaigns
        return ResponseEntity.ok(List.of());
    }

    // View all charity events
    @GetMapping("/charity-events")
    public ResponseEntity<List<?>> getAllCharityEvents() {
        // TODO: Return all charity events
        return ResponseEntity.ok(List.of());
    }

    // View all donation events
    @GetMapping("/donation-events")
    public ResponseEntity<List<?>> getAllDonationEvents() {
        // TODO: Return all donation events
        return ResponseEntity.ok(List.of());
    }

    // View all pending requests
    @GetMapping("/pending-requests")
    public ResponseEntity<List<?>> getAllPendingRequests() {
        // TODO: Return all pending requests
        return ResponseEntity.ok(List.of());
    }

    // View all approved requests
    @GetMapping("/approved-requests")
    public ResponseEntity<List<?>> getAllApprovedRequests() {
        // TODO: Return all approved requests
        return ResponseEntity.ok(List.of());
    }

    // View all rejected requests
    @GetMapping("/rejected-requests")
    public ResponseEntity<List<?>> getAllRejectedRequests() {
        // TODO: Return all rejected requests
        return ResponseEntity.ok(List.of());
    }

    // View all user roles
    @GetMapping("/roles")
    public ResponseEntity<List<?>> getAllRoles() {
        // TODO: Return all user roles
        return ResponseEntity.ok(List.of());
    }

    // View all account statuses
    @GetMapping("/account-statuses")
    public ResponseEntity<List<?>> getAllAccountStatuses() {
        // TODO: Return all account statuses
        return ResponseEntity.ok(List.of());
    }

    // View all event statuses
    @GetMapping("/event-statuses")
    public ResponseEntity<List<?>> getAllEventStatuses() {
        // TODO: Return all event statuses
        return ResponseEntity.ok(List.of());
    }

    // View all request statuses
    @GetMapping("/request-statuses")
    public ResponseEntity<List<?>> getAllRequestStatuses() {
        // TODO: Return all request statuses
        return ResponseEntity.ok(List.of());
    }

    // View all organization statuses
    @GetMapping("/organization-statuses")
    public ResponseEntity<List<?>> getAllOrganizationStatuses() {
        // TODO: Return all organization statuses
        return ResponseEntity.ok(List.of());
    }

    // View all admin roles
    @GetMapping("/admin-roles")
    public ResponseEntity<List<?>> getAllAdminRoles() {
        // TODO: Return all admin roles
        return ResponseEntity.ok(List.of());
    }

    // View all volunteer roles
    @GetMapping("/volunteer-roles")
    public ResponseEntity<List<?>> getAllVolunteerRoles() {
        // TODO: Return all volunteer roles
        return ResponseEntity.ok(List.of());
    }

    // View all organization roles
    @GetMapping("/organization-roles")
    public ResponseEntity<List<?>> getAllOrganizationRoles() {
        // TODO: Return all organization roles
        return ResponseEntity.ok(List.of());
    }

    // View all donation roles
    @GetMapping("/donation-roles")
    public ResponseEntity<List<?>> getAllDonationRoles() {
        // TODO: Return all donation roles
        return ResponseEntity.ok(List.of());
    }

    // View all charity roles
    @GetMapping("/charity-roles")
    public ResponseEntity<List<?>> getAllCharityRoles() {
        // TODO: Return all charity roles
        return ResponseEntity.ok(List.of());
    }

    // View all event types
    @GetMapping("/event-types")
    public ResponseEntity<List<?>> getAllEventTypes() {
        // TODO: Return all event types
        return ResponseEntity.ok(List.of());
    }

    // View all request types
    @GetMapping("/request-types")
    public ResponseEntity<List<?>> getAllRequestTypes() {
        // TODO: Return all request types
        return ResponseEntity.ok(List.of());
    }

    // View all organization types
    @GetMapping("/organization-types")
    public ResponseEntity<List<?>> getAllOrganizationTypes() {
        // TODO: Return all organization types
        return ResponseEntity.ok(List.of());
    }

    // View all donation types
    @GetMapping("/donation-types")
    public ResponseEntity<List<?>> getAllDonationTypes() {
        // TODO: Return all donation types
        return ResponseEntity.ok(List.of());
    }

    // View all charity types
    @GetMapping("/charity-types")
    public ResponseEntity<List<?>> getAllCharityTypes() {
        // TODO: Return all charity types
        return ResponseEntity.ok(List.of());
    }
} 