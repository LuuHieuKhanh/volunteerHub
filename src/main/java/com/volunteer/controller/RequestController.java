package com.volunteer.controller;

import com.volunteer.dto.organization.OrganizationRequest;
import com.volunteer.dto.request.OrganizationRegisterRequest;
import com.volunteer.dto.request.OrganizationRegisterResponse;
import com.volunteer.dto.request.RequestResponse;
import com.volunteer.dto.request.UpdateRequestStatusDto;
import com.volunteer.entity.Account;
import com.volunteer.entity.Organization;
import com.volunteer.entity.Request;
import com.volunteer.entity.Volunteer;
import com.volunteer.enums.ERequestType;
import com.volunteer.enums.RequestStatus;
import com.volunteer.enums.Role;
import com.volunteer.exception.ResourceAlreadyExistsException;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.AccountRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.RequestRepository;
import com.volunteer.repository.VolunteerRepository;
import com.volunteer.service.LocalStorageService;
import com.volunteer.service.OrganizationService;
import com.volunteer.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private LocalStorageService localStorageService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private AccountRepository accountRepository;


    @Transactional()
    @PostMapping()
    public ResponseEntity<OrganizationRegisterResponse> createRequest(@ModelAttribute OrganizationRegisterRequest dto) throws IOException {
        logger.info("RequestController createRequest");

        // 1. Tìm volunteer theo volunteerId từ DTO
        Volunteer volunteer = volunteerRepository.findById(dto.getVolunteerId())
                .orElseThrow(() -> new RuntimeException("Volunteer not found with ID: " + dto.getVolunteerId()));

        List<RequestStatus> statuses = List.of(RequestStatus.PENDING, RequestStatus.APPROVED);
        Optional<Request> existRequestPending = requestRepository.findByVolunteer_IdAndStatusIn(dto.getVolunteerId(), statuses);
        if(existRequestPending.isPresent()) {
            throw new ResourceAlreadyExistsException("Volunteer đã có yêu cầu đang chờ xử lý hoặc đã được duyệt");
        }


        Optional<Organization> organizationOpt = organizationRepository.findByOrganizationName(dto.getOrganizationName());
        if (organizationOpt.isPresent()) {
            throw new ResourceAlreadyExistsException("Tổ chức với tên '" + dto.getOrganizationName() + "' đã tồn tại.");
        }

        // 2. Upload file logo nếu có
        String logoUrl = null;
        if (dto.getOrganizationLogo() != null && !dto.getOrganizationLogo().isEmpty()) {
            logoUrl = localStorageService.uploadFile(dto.getOrganizationLogo());
        }

        // 3. Upload file chứng chỉ nếu có
        String certificateUrl = null;
        if (dto.getCertificate() != null && !dto.getCertificate().isEmpty()) {
            certificateUrl = localStorageService.uploadFile(dto.getCertificate());
        }

        OrganizationRequest organizationRequest = new OrganizationRequest();
        organizationRequest.setOrganizationName(dto.getOrganizationName());
        organizationRequest.setDescription(Boolean.parseBoolean(dto.getDescription()) ? dto.getDescription() : "");
        organizationRequest.setLogo(logoUrl);
        organizationRequest.setCertificate(certificateUrl);
        organizationRequest.setVolunteerId(volunteer.getId());

        Organization savedOrganization = organizationService.createOrganization(organizationRequest);

        Request request = Request.builder()
                .status(RequestStatus.PENDING)
                .requestType(ERequestType.ORGANIZATION_REGISTRATION)
                .denyReason(dto.getReason())
                .volunteer(volunteer) // Cần method để lấy volunteer hiện tại
                .organization(savedOrganization) // sẽ gán sau nếu có logic tạo tổ chức
                .build();;

        Request savedRequest = requestService.createRequest(request);

        OrganizationRegisterResponse response = OrganizationRegisterResponse.builder()
                .id(savedRequest.getId())
                .organizationName(dto.getOrganizationName())
                .email(volunteer.getAccount().getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .status(RequestStatus.valueOf(savedRequest.getStatus().name()))
                .logoUrl(logoUrl)
                .certificateUrl(certificateUrl)
                .message("Request created successfully!")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{volunteerId}")
    public ResponseEntity<List<RequestResponse>> viewOwnRequests(@PathVariable("volunteerId") Long volunteerId) {
        logger.info("View own requests for volunteer: {}", volunteerId);
        List<Request> requests = requestRepository.findByVolunteer_Id(volunteerId);

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

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatusRequest(@PathVariable("id") Long id, @RequestBody UpdateRequestStatusDto dto) {
        logger.info("Update status request for id: {}", id);
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));

        RequestStatus status = dto.getStatus();

        request.setStatus(status);
        requestRepository.save(request);

        if(status == RequestStatus.APPROVED) {
            Account account = accountRepository.findById(request.getVolunteer().getId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
            account.setRole(Role.ROLE_ORGANIZATION);
            accountRepository.save(account);
        }
        return ResponseEntity.ok(request);
    }
}
