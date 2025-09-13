package com.volunteer.service;

import com.volunteer.dto.organization.*;
import com.volunteer.dto.event.CharityEventResponse;
import com.volunteer.dto.donation.DonationEventResponse;
import com.volunteer.entity.*;
import com.volunteer.enums.*;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CharityEventRepository charityEventRepository;
    @Autowired
    private DonationEventRepository donationEventRepository;
    @Autowired
    private VolunteerCharityEventRepository volunteerCharityEventRepository;
    @Autowired
    private VolunteerDonationRepository volunteerDonationRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Organization createOrganization(OrganizationRequest request) {
        logger.info("Creating organization: {}", request.getOrganizationName());
        Volunteer volunteer = volunteerRepository.findById(request.getVolunteerId())
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + request.getVolunteerId()));
        Organization org = new Organization();
        org.setOrganizationName(request.getOrganizationName());
        org.setDescription(request.getDescription());
        org.setLogo(request.getLogo());
        org.setCertificate(request.getCertificate());
        org.setVolunteer(volunteer);
        Organization saved = organizationRepository.save(org);
        return saved;
    }

    public OrganizationResponse getOrganizationById(Long id) {
        logger.info("Fetching organization by id: {}", id);
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        return toResponse(org);
    }

    @Transactional
    public OrganizationResponse updateOrganization(Long id, OrganizationRequest request) {
        logger.info("Updating organization id: {}", id);
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        org.setOrganizationName(request.getOrganizationName());
        org.setDescription(request.getDescription());
        Organization saved = organizationRepository.save(org);
        return toResponse(saved);
    }

    @Transactional
    public void deleteOrganization(Long id) {
        logger.info("Deleting organization id: {}", id);
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        organizationRepository.delete(org);
    }

    // Admin CRUD methods
    public List<OrganizationListResponse> getAllOrganizations(String search) {
        logger.info("Getting all organizations with search: {}", search);
        List<Organization> organizations;
        if (StringUtils.hasText(search)) {
            organizations = organizationRepository.findByOrganizationNameContainingIgnoreCase(search);
        } else {
            organizations = organizationRepository.findAll();
        }

        // Filter only organizations with approved requests and not deleted
        return organizations.stream()
                .filter(org -> {
                    // Check if this organization has an approved request and is not deleted
                    return !org.isDeleted() && requestRepository.existsByOrganizationAndStatus(org, RequestStatus.APPROVED);
                })
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationListResponse createOrganizationByAdmin(OrganizationCreateRequest request, boolean isAdmin) {
        logger.info("Creating organization by admin: {}", request.getOrganizationName());

        // Find volunteer by ID (owner already exists)
        Volunteer ownerVolunteer = volunteerRepository.findById(request.getVolunteerId())
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + request.getVolunteerId()));

        // Create organization
        Organization organization = new Organization();
        organization.setOrganizationName(request.getOrganizationName());
        organization.setDescription(request.getDescription());
        organization.setCertificate(request.getCertificate());
        organization.setLogo(request.getLogo());
        organization.setVolunteer(ownerVolunteer);
        organization = organizationRepository.save(organization);

        // Create request (approved if admin created)
        Request requestEntity = new Request();
        requestEntity.setRequestType(ERequestType.ORGANIZATION_REGISTRATION);
        requestEntity.setVolunteer(ownerVolunteer);
        requestEntity.setOrganization(organization);
        requestEntity.setStatus(isAdmin ? RequestStatus.APPROVED : RequestStatus.PENDING);
        requestRepository.save(requestEntity);

        return toListResponse(organization);
    }

    public OrganizationDetailResponse getOrganizationDetail(Long id) {
        logger.info("Getting organization detail for id: {}", id);
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));

        OrganizationDetailResponse response = new OrganizationDetailResponse();
        response.setId(organization.getId());
        response.setOrganizationName(organization.getOrganizationName());
        response.setDescription(organization.getDescription());
        response.setCertificate(organization.getCertificate());
        response.setLogo(organization.getLogo());
        response.setCreatedAt(organization.getCreatedAt());
        response.setUpdatedAt(organization.getUpdatedAt());
        response.setDeleted(organization.isDeleted());

        // Owner information
        if (organization.getVolunteer() != null) {
            Volunteer owner = organization.getVolunteer();
            response.setOwnerName(owner.getFullName());
            response.setOwnerContact(owner.getContact());
            if (owner.getAccount() != null) {
                response.setOwnerEmail(owner.getAccount().getEmail());
                response.setOwnerStatus(null); // Account doesn't have status field
                response.setOwnerIsActive(owner.getAccount().isActive());
                response.setOwnerIsBanned(owner.isBanned());
            }
        }

        // Statistics
        List<CharityEvent> charityEvents = charityEventRepository.findByOrganization_Id(id);
        List<DonationEvent> donationEvents = donationEventRepository.findByOrganization_Id(id);

        // Count total volunteers participated in charity events
        Long totalVolunteers = charityEvents.stream()
                .mapToLong(event -> volunteerCharityEventRepository.countByCharityEventId(event.getId()))
                .sum();
        response.setTotalVolunteersParticipated(totalVolunteers);

        // Count total donation amount
        BigDecimal totalDonationAmount = donationEvents.stream()
                .flatMap(event -> volunteerDonationRepository.findByDonationEventId(event.getId()).stream())
                .map(donation -> donation.getDonateAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalDonationAmount(totalDonationAmount);

        response.setTotalCharityEvents((long) charityEvents.size());
        response.setTotalDonationEvents((long) donationEvents.size());

        // Charity events with volunteer count
        List<CharityEventResponse> charityEventResponses = charityEvents.stream()
                .map(event -> {
                    CharityEventResponse eventResponse = new CharityEventResponse(
                            event.getId(),
                            event.getCharityName(),
                            event.getOrganization().getId(),
                            event.getDescription(),
                            event.getDestination(),
                            event.getDateStart(),
                            event.getDateEnd(),
                            event.getNumVolunteerRequire(),
                            event.getNumVolunteerActual(),
                            event.getNote(),
                            event.getPic(),
                            event.getEventStatus()
                    );
                    eventResponse.setJoinStatus(null); // joinStatus not applicable for organization view
                    return eventResponse;
                })
                .collect(Collectors.toList());
        response.setCharityEvents(charityEventResponses);

        // Donation events with actual amount
        List<DonationEventResponse> donationEventResponses = donationEvents.stream()
                .map(event -> {
                    BigDecimal actualAmount = volunteerDonationRepository.findByDonationEventId(event.getId())
                            .stream()
                            .map(donation -> donation.getDonateAmount())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    DonationEventResponse eventResponse = new DonationEventResponse();
                    eventResponse.setId(event.getId());
                    eventResponse.setDonationName(event.getTitle());
                    eventResponse.setOrganizationId(event.getOrganization().getId());
                    eventResponse.setDescription(event.getDescription());
                    eventResponse.setDestination(""); // DonationEvent doesn't have destination
                    eventResponse.setDateStart(null); // DonationEvent doesn't have dateStart
                    eventResponse.setDateEnd(null); // DonationEvent doesn't have dateEnd
                    eventResponse.setTargetAmount(event.getMoneyNeed());
                    eventResponse.setActualAmount(actualAmount);
                    eventResponse.setNote(event.getNote());
                    eventResponse.setPic(event.getPic());
                    eventResponse.setEventStatus(event.getEventStatus());
                    eventResponse.setCreatedAt(event.getCreatedAt());
                    eventResponse.setUpdatedAt(event.getUpdatedAt());
                    return eventResponse;
                })
                .collect(Collectors.toList());
        response.setDonationEvents(donationEventResponses);

        return response;
    }

    @Transactional
    public OrganizationListResponse updateOrganizationByAdmin(Long id, OrganizationUpdateRequest request) {
        logger.info("Updating organization id: {}", id);
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));

        if (StringUtils.hasText(request.getOrganizationName())) {
            organization.setOrganizationName(request.getOrganizationName());
        }
        if (StringUtils.hasText(request.getDescription())) {
            organization.setDescription(request.getDescription());
        }
        if (StringUtils.hasText(request.getCertificate())) {
            organization.setCertificate(request.getCertificate());
        }
        if (StringUtils.hasText(request.getLogo())) {
            organization.setLogo(request.getLogo());
        }

        organization = organizationRepository.save(organization);
        return toListResponse(organization);
    }

    @Transactional
    public void softDeleteOrganizationByAdmin(Long id) {
        logger.info("Soft deleting organization id: {}", id);
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));

        organization.setDeleted(true);
        organization.setDeletedAt(LocalDateTime.now());
        organizationRepository.save(organization);
    }

    private OrganizationListResponse toListResponse(Organization organization) {
        OrganizationListResponse response = new OrganizationListResponse();
        response.setId(organization.getId());
        response.setOrganizationName(organization.getOrganizationName());
        response.setDescription(organization.getDescription());
        response.setCertificate(organization.getCertificate());
        response.setLogo(organization.getLogo());
        response.setCreatedAt(organization.getCreatedAt());
        response.setUpdatedAt(organization.getUpdatedAt());
        response.setDeleted(organization.isDeleted());

        // Owner information
        if (organization.getVolunteer() != null) {
            Volunteer owner = organization.getVolunteer();
            response.setOwnerName(owner.getFullName());
            response.setOwnerContact(owner.getContact());
            if (owner.getAccount() != null) {
                response.setOwnerEmail(owner.getAccount().getEmail());
                response.setOwnerStatus(null); // Account doesn't have status field
                response.setOwnerIsActive(owner.getAccount().isActive());
                response.setOwnerIsBanned(owner.isBanned());
            }
        }

        return response;
    }

    private OrganizationResponse toResponse(Organization org) {
        return new OrganizationResponse(
                org.getId(),
                org.getOrganizationName(),
                org.getDescription(),
                org.getVolunteer() != null ? org.getVolunteer().getId() : null
        );
    }
}
