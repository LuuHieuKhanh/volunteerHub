package com.volunteer.service;

import com.volunteer.dto.donation.*;
import com.volunteer.entity.DonationEvent;
import com.volunteer.entity.Organization;
import com.volunteer.entity.VolunteerDonation;
import com.volunteer.enums.EEventStatus;
import com.volunteer.enums.RequestStatus;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.DonationEventRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.RequestRepository;
import com.volunteer.repository.VolunteerDonationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationEventService {

    private static final Logger logger = LoggerFactory.getLogger(DonationEventService.class);

    @Autowired
    private DonationEventRepository donationEventRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private VolunteerDonationRepository volunteerDonationRepository;

    @Autowired
    private RequestRepository requestRepository;

    public List<DonationEventListResponse> getAllDonationEvents(String search) {
        logger.info("Getting all donation events with search: {}", search);
        List<DonationEvent> events;

        if (StringUtils.hasText(search)) {
            events = donationEventRepository.findByTitleContainingIgnoreCaseOrOrganization_OrganizationNameContainingIgnoreCase(search, search);
        } else {
            events = donationEventRepository.findAll();
        }

        return events.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    public DonationEventDetailResponse getDonationEventDetail(Long id) {
        logger.info("Getting donation event detail for id: {}", id);
        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));

        return toDetailResponse(event);
    }

    @Transactional
    public DonationEventListResponse createDonationEvent(DonationEventCreateRequest request) {
        logger.info("Creating donation event: {}", request.getTitle());

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));

        DonationEvent event = DonationEvent.builder()
                .organization(organization)
                .title(request.getTitle())
                .description(request.getDescription())
                .moneyNeed(request.getMoneyNeed())
                .eventStatus(request.getEventStatus())
                .hasDonate(request.isHasDonate())
                .note(request.getNote())
                .qrPic(request.getQrPic())
                .bankAccount(request.getBankAccount())
                .pic(request.getPic())
                .build();

        DonationEvent savedEvent = donationEventRepository.save(event);
        return toListResponse(savedEvent);
    }

    @Transactional
    public DonationEventListResponse updateDonationEvent(Long id, DonationEventUpdateRequest request) {
        logger.info("Updating donation event id: {}", id);

        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));

        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));
            event.setOrganization(organization);
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getMoneyNeed() != null) {
            event.setMoneyNeed(request.getMoneyNeed());
        }
        if (request.getEventStatus() != null) {
            event.setEventStatus(request.getEventStatus());
        }
        event.setHasDonate(request.isHasDonate());
        if (request.getNote() != null) {
            event.setNote(request.getNote());
        }
        if (request.getQrPic() != null) {
            event.setQrPic(request.getQrPic());
        }
        if (request.getBankAccount() != null) {
            event.setBankAccount(request.getBankAccount());
        }
        if (request.getPic() != null) {
            event.setPic(request.getPic());
        }

        DonationEvent savedEvent = donationEventRepository.save(event);
        return toListResponse(savedEvent);
    }

    @Transactional
    public void softDeleteDonationEvent(Long id) {
        logger.info("Soft deleting donation event id: {}", id);
        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));

        event.setDeleted(true);
        event.setDeletedAt(LocalDateTime.now());
        donationEventRepository.save(event);
    }

    @Transactional
    public DonationEventListResponse updateDonationEventStatus(Long id, DonationEventStatusUpdateRequest request) {
        logger.info("Updating donation event status for id: {} to {}", id, request.getEventStatus());

        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));

        event.setEventStatus(request.getEventStatus());
        DonationEvent savedEvent = donationEventRepository.save(event);

        return toListResponse(savedEvent);
    }

    private DonationEventListResponse toListResponse(DonationEvent event) {
        DonationEventListResponse response = new DonationEventListResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setMoneyNeed(event.getMoneyNeed());
        response.setEventStatus(event.getEventStatus());
        response.setHasDonate(event.isHasDonate());
        response.setNote(event.getNote());
        response.setQrPic(event.getQrPic());
        response.setBankAccount(event.getBankAccount());
        response.setPic(event.getPic());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());

        // Organization info
        if (event.getOrganization() != null) {
            Organization org = event.getOrganization();
            response.setOrganizationId(org.getId());
            response.setOrganizationName(org.getOrganizationName());
            response.setOrganizationDescription(org.getDescription());
        }

        // Calculate total donated amount
        List<VolunteerDonation> donations = volunteerDonationRepository.findByDonationEventId(event.getId());
        BigDecimal totalDonated = donations.stream()
                .map(VolunteerDonation::getDonateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalDonatedAmount(totalDonated);

        return response;
    }

    private DonationEventDetailResponse toDetailResponse(DonationEvent event) {
        DonationEventDetailResponse response = new DonationEventDetailResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setMoneyNeed(event.getMoneyNeed());
        response.setEventStatus(event.getEventStatus());
        response.setHasDonate(event.isHasDonate());
        response.setNote(event.getNote());
        response.setQrPic(event.getQrPic());
        response.setBankAccount(event.getBankAccount());
        response.setPic(event.getPic());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());

        // Organization info
        if (event.getOrganization() != null) {
            Organization org = event.getOrganization();
            response.setOrganizationId(org.getId());
            response.setOrganizationName(org.getOrganizationName());
            response.setOrganizationDescription(org.getDescription());
        }

        // Calculate total donated amount and get donor list
        List<VolunteerDonation> donations = volunteerDonationRepository.findByDonationEventId(event.getId());
        BigDecimal totalDonated = donations.stream()
                .map(VolunteerDonation::getDonateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalDonatedAmount(totalDonated);

        // Map donor list
        List<DonorResponse> donorResponses = donations.stream()
                .map(donation -> {
                    DonorResponse donorResponse = new DonorResponse();
                    donorResponse.setId(donation.getId());
                    donorResponse.setDonateAmount(donation.getDonateAmount());
                    donorResponse.setNote(donation.getNote());
                    donorResponse.setCreatedAt(donation.getCreatedAt());
                    donorResponse.setUpdatedAt(donation.getUpdatedAt());

                    if (donation.getVolunteer() != null) {
                        donorResponse.setVolunteerId(donation.getVolunteer().getId());
                        donorResponse.setVolunteerFullName(donation.getVolunteer().getFullName());
                        donorResponse.setVolunteerContact(donation.getVolunteer().getContact());
                        if (donation.getVolunteer().getAccount() != null) {
                            donorResponse.setVolunteerEmail(donation.getVolunteer().getAccount().getEmail());
                        }
                    }

                    return donorResponse;
                })
                .collect(Collectors.toList());
        response.setDonors(donorResponses);

        // Get available organizations (with approved requests)
        List<Organization> allOrganizations = organizationRepository.findAll();
        List<OrganizationOptionResponse> availableOrganizations = allOrganizations.stream()
                .filter(org -> !org.isDeleted() && requestRepository.existsByOrganizationAndStatus(org, RequestStatus.APPROVED))
                .map(org -> {
                    OrganizationOptionResponse orgResponse = new OrganizationOptionResponse();
                    orgResponse.setId(org.getId());
                    orgResponse.setOrganizationName(org.getOrganizationName());
                    orgResponse.setDescription(org.getDescription());
                    return orgResponse;
                })
                .collect(Collectors.toList());
        response.setAvailableOrganizations(availableOrganizations);

        return response;
    }

    public List<VolunteerDonationHistoryResponse> getVolunteerDonationHistory(Long volunteerId) {
        logger.info("Getting donation history for volunteer id: {}", volunteerId);

        List<VolunteerDonation> donations = volunteerDonationRepository.findByVolunteer_Id(volunteerId);

        return donations.stream()
                .map(donation -> {
                    VolunteerDonationHistoryResponse response = new VolunteerDonationHistoryResponse();

                    // Donation info
                    response.setId(donation.getId());
                    response.setDonateAmount(donation.getDonateAmount());
                    response.setDonationNote(donation.getNote());
                    response.setDonationCreatedAt(donation.getCreatedAt());
                    response.setDonationUpdatedAt(donation.getUpdatedAt());

                    // Donation event info
                    if (donation.getDonationEvent() != null) {
                        DonationEvent event = donation.getDonationEvent();
                        response.setDonationEventId(event.getId());
                        response.setDonationEventTitle(event.getTitle());
                        response.setDonationEventDescription(event.getDescription());
                        response.setMoneyNeed(event.getMoneyNeed());
                        response.setEventStatus(event.getEventStatus());
                        response.setNote(event.getNote());
                        response.setQrPic(event.getQrPic());
                        response.setBankAccount(event.getBankAccount());
                        response.setPic(event.getPic());
                        response.setEventCreatedAt(event.getCreatedAt());
                        response.setEventUpdatedAt(event.getUpdatedAt());

                        // Organization info
                        if (event.getOrganization() != null) {
                            Organization org = event.getOrganization();
                            response.setOrganizationId(org.getId());
                            response.setOrganizationName(org.getOrganizationName());
                            response.setOrganizationDescription(org.getDescription());
                        }
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }
}
