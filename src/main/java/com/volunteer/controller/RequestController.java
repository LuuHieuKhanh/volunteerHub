package com.volunteer.controller;

import com.volunteer.dto.request.OrganizationUpgradeRequest;
import com.volunteer.dto.request.RequestCreateUpdateEvent;
import com.volunteer.dto.request.RequestResponse;
import com.volunteer.entity.Request;
import com.volunteer.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private RequestRepository requestRepository;

    // TODO: Implement request management endpoints

    // Submit organization upgrade request
    @PostMapping("/organization-upgrade")
    public ResponseEntity<String> submitOrganizationUpgradeRequest(@RequestBody OrganizationUpgradeRequest upgradeRequest) {
        // Here you would call a service to handle the request creation
        // For now, just log and return success
        logger.info("Organization upgrade request submitted: {}", upgradeRequest);
        return ResponseEntity.ok("Organization upgrade request submitted");
    }

    // Submit event management request (add/update/delete event or donation program)
    @PostMapping("/event-management")
    public ResponseEntity<String> submitEventManagementRequest(@RequestBody RequestCreateUpdateEvent eventManagementRequest) {
        // Here you would call a service to handle the request creation
        // For now, just log and return success
        logger.info("Event management request submitted: {}", eventManagementRequest);
        return ResponseEntity.ok("Event management request submitted");
    }

    // View own requests
    @GetMapping("/my")
    public ResponseEntity<List<RequestResponse>> viewOwnRequests(@RequestParam Long volunteerId) {
        List<Request> requests = requestRepository.findByVolunteer_Id(volunteerId);
        List<RequestResponse> response = requests.stream()
                .map(req -> new RequestResponse(
                        req.getId(),
                        req.getRequestType(),
                        req.getVolunteer() != null ? req.getVolunteer().getId() : null,
                        req.getOrganization() != null ? req.getOrganization().getId() : null,
                        req.getStatus(),
                        req.getDenyReason(),
                        req.getPic(),
                        req.getCreatedAt()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
} 