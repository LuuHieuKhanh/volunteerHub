package com.volunteer.service;

import com.volunteer.dto.request.RequestDetailResponse;
import com.volunteer.dto.request.RequestListResponse;
import com.volunteer.dto.request.RequestStatusUpdateRequest;
import com.volunteer.dto.request.MessageResponse;
import com.volunteer.entity.Request;
import com.volunteer.entity.Organization;
import com.volunteer.entity.Volunteer;
import com.volunteer.enums.RequestStatus;
import com.volunteer.enums.Role;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.RequestRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.VolunteerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<RequestListResponse> getAllRequests(String search) {
        logger.info("Getting all requests with search: {}", search);
        List<Request> requests;

        if (StringUtils.hasText(search)) {
            requests = requestRepository.findByOrganization_OrganizationNameContainingIgnoreCase(search);
        } else {
            requests = requestRepository.findAll();
        }

        return requests.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    public List<RequestListResponse> getPendingRequests() {
        logger.info("Getting all pending requests");
        List<Request> requests = requestRepository.findByStatus(RequestStatus.PENDING);

        return requests.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    public RequestDetailResponse getRequestDetail(Long id) {
        logger.info("Getting request detail for id: {}", id);
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));

        return toDetailResponse(request);
    }

    @Transactional
    public MessageResponse updateRequestStatus(Long id, RequestStatusUpdateRequest request) {
        logger.info("Updating request status for id: {} to status: {}", id, request.getStatus());

        Request existingRequest = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));

        existingRequest.setStatus(request.getStatus());

        if (request.getStatus() == RequestStatus.REJECTED) {
            if (request.getDenyReason() == null || request.getDenyReason().trim().isEmpty()) {
                throw new IllegalArgumentException("Deny reason is required when rejecting a request");
            }
            existingRequest.setDenyReason(request.getDenyReason());
        } else if (request.getStatus() == RequestStatus.APPROVED) {
            // Clear deny reason when approving
            existingRequest.setDenyReason(null);

            // If this is an organization registration request, update volunteer role
            if (existingRequest.getRequestType().name().equals("ORGANIZATION_REGISTRATION")) {
                Volunteer volunteer = existingRequest.getVolunteer();
                if (volunteer != null && volunteer.getAccount() != null) {
                    volunteer.getAccount().setRole(Role.ROLE_ORGANIZATION);
                    volunteerRepository.save(volunteer);
                }
            }
        }

        requestRepository.save(existingRequest);

        String message = request.getStatus() == RequestStatus.APPROVED
                ? "Request approved successfully" : "Request rejected successfully";

        return new MessageResponse(message);
    }

    private RequestListResponse toListResponse(Request request) {
        RequestListResponse response = new RequestListResponse();
        response.setId(request.getId());
        response.setRequestType(request.getRequestType());
        response.setStatus(request.getStatus());
        response.setDenyReason(request.getDenyReason());
        response.setCreatedAt(request.getCreatedAt());
        response.setUpdatedAt(request.getUpdatedAt());

        // Organization info
        if (request.getOrganization() != null) {
            Organization org = request.getOrganization();
            response.setOrganizationId(org.getId());
            response.setOrganizationName(org.getOrganizationName());
            response.setOrganizationDescription(org.getDescription());
            response.setOrganizationContact(""); // Organization entity doesn't have contact field
            response.setOrganizationAddress(""); // Organization entity doesn't have address field
        }

        // Volunteer info
        if (request.getVolunteer() != null) {
            Volunteer volunteer = request.getVolunteer();
            response.setVolunteerId(volunteer.getId());
            response.setVolunteerFullName(volunteer.getFullName());
            response.setVolunteerContact(volunteer.getContact());
            if (volunteer.getAccount() != null) {
                response.setVolunteerEmail(volunteer.getAccount().getEmail());
            }
        }

        return response;
    }

    private RequestDetailResponse toDetailResponse(Request request) {
        RequestDetailResponse response = new RequestDetailResponse();
        response.setId(request.getId());
        response.setRequestType(request.getRequestType());
        response.setStatus(request.getStatus());
        response.setDenyReason(request.getDenyReason());
        response.setCreatedAt(request.getCreatedAt());
        response.setUpdatedAt(request.getUpdatedAt());

        // Organization info
        if (request.getOrganization() != null) {
            Organization org = request.getOrganization();
            response.setOrganizationId(org.getId());
            response.setOrganizationName(org.getOrganizationName());
            response.setOrganizationDescription(org.getDescription());
            response.setOrganizationContact(""); // Organization entity doesn't have contact field
            response.setOrganizationAddress(""); // Organization entity doesn't have address field
            response.setOrganizationEmail(""); // Organization entity doesn't have email field
            response.setOrganizationWebsite(""); // Organization entity doesn't have website field
            response.setOrganizationLogo(org.getLogo());
            response.setOrganizationCreatedAt(org.getCreatedAt());
            response.setOrganizationUpdatedAt(org.getUpdatedAt());
        }

        // Volunteer info
        if (request.getVolunteer() != null) {
            Volunteer volunteer = request.getVolunteer();
            response.setVolunteerId(volunteer.getId());
            response.setVolunteerFullName(volunteer.getFullName());
            response.setVolunteerContact(volunteer.getContact());
            response.setVolunteerAddress(""); // Volunteer entity doesn't have address field
            response.setVolunteerAvatar(volunteer.getPic()); // Volunteer entity uses 'pic' field instead of 'avatar'
            response.setVolunteerCreatedAt(volunteer.getCreatedAt());
            response.setVolunteerUpdatedAt(volunteer.getUpdatedAt());
            if (volunteer.getAccount() != null) {
                response.setVolunteerEmail(volunteer.getAccount().getEmail());
            }
        }

        return response;
    }
}
