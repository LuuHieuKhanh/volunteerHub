package com.volunteer.service;

import com.volunteer.dto.donation.*;
import com.volunteer.dto.event.DonationEventRequest;
import com.volunteer.dto.event.DonationEventResponse;
import com.volunteer.dto.event.VolunteerDonationResponse;
import com.volunteer.entity.*;
import com.volunteer.enums.EEventStatus;
import com.volunteer.enums.ERequestType;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private LocalStorageService localStorageService;
    @Autowired
    private VolunteerDonationRepository volunteerDonationRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RequestService requestService;

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
    public DonationEventResponse createDonationEvent(DonationEventRequest request) throws IOException {
        logger.info("Creating donation event: {}", request.getTitle());

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));

        DonationEvent event = new DonationEvent();
        event.setOrganization(org);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setMoneyNeed(request.getMoneyNeed());
        event.setNote(request.getNote());
        event.setBankAccount(request.getBankAccount());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());
        event.setEventStatus(EEventStatus.PENDING);
        // ✅ Xử lý upload file ảnh QR
        if (request.getQrPic() != null && !request.getQrPic().isEmpty()) {
            String qrPath = localStorageService.uploadFile(request.getQrPic());
            event.setQrPic(qrPath);
        }

        // ✅ Xử lý upload ảnh banner
        if (request.getPic() != null && !request.getPic().isEmpty()) {
            String picPath = localStorageService.uploadFile(request.getPic());
            event.setPic(picPath);
        }

        DonationEvent saved = donationEventRepository.save(event);


        Request req = Request.builder()
                .status(RequestStatus.PENDING)
                .requestType(ERequestType.DONATION_REGISTRATION)
                .volunteer(org.getVolunteer())
                .organization(org)
                .donationEvent(saved)
                .build();

        Request savedRequest = requestService.createRequest(req);
        return toResponse(saved, BigDecimal.valueOf(0));
    }

    public DonationEventResponse getDonationEventById(Long id) {
        logger.info("Fetching donation event by id: {}", id);
        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));
        return toResponse(event, BigDecimal.valueOf(0));
    }

    public List<DonationEventResponse> getListDonation() {
        List<DonationEvent> events = donationEventRepository.findAll();

        return events.stream().map(event -> {
            BigDecimal totalDonated = event.getDonorList() != null
                    ? event.getDonorList().stream()
                    .map(VolunteerDonation::getDonateAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    : BigDecimal.ZERO;

            return new DonationEventResponse(
                    event.getId(),
                    event.getOrganization().getId(),
                    event.getOrganization().getOrganizationName(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getMoneyNeed(),
                    event.getEventStatus().name(),
                    false, // 👈 hasDonate: bạn muốn check theo user thì truyền userId vào
                    event.getNote(),
                    event.getQrPic(),
                    event.getBankAccount(),
                    localStorageService.getFullFileUrl(event.getPic()),
                    event.getDateStart(),
                    event.getDateEnd(),
                    totalDonated
            );
        }).toList();
    }


    public List<VolunteerDonationResponse> getVolunteersByDonationEvent(Long eventId) {
        List<VolunteerDonation> donations = volunteerDonationRepository.findByDonationEventId(eventId);

        return donations.stream().map(vd -> {
            Volunteer v = vd.getVolunteer();
            return VolunteerDonationResponse.builder()
                    .id(v.getId())
                    .fullName(v.getFullName())
                    .email(v.getAccount().getEmail())
                    .contact(v.getContact())
                    .donateAmount(vd.getDonateAmount())
                    .donateTime(vd.getCreatedAt()) // lấy từ BaseEntity
                    .note(vd.getNote())
                    .build();
        }).toList();
    }

    public List<DonationEventResponse> getDonationsByOrganization(
            Long organizationId, String name, String from, String to) {

        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (from != null && !from.isEmpty()) {
            fromDateTime = LocalDate.parse(from, formatter).atStartOfDay();
        }
        if (to != null && !to.isEmpty()) {
            toDateTime = LocalDate.parse(to, formatter).atTime(23, 59, 59);
        }

        List<DonationEvent> events = donationEventRepository.searchDonationsByOrganization(
                organizationId, name, fromDateTime, toDateTime
        );

        return events.stream()
                .map(event -> {
                    BigDecimal totalDonated =
                            volunteerDonationRepository.getTotalDonationByEvent(event.getId());
                    return toResponse(event, totalDonated);
                })
                .toList();
    }

    @Transactional
    public DonationEventListResponse updateDonationEventAdmin(Long id, DonationEventUpdateRequest request) {
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
    public DonationEventResponse updateDonationEvent(Long id, DonationEventRequest request) throws IOException {
        logger.info("Updating donation event id: {}", id);

        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));

        // update basic fields
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setMoneyNeed(request.getMoneyNeed());
        event.setNote(request.getNote());
        event.setBankAccount(request.getBankAccount());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());

        // xử lý upload QR mới (nếu có)
        if (request.getQrPic() != null && !request.getQrPic().isEmpty()) {
            String qrPath = localStorageService.uploadFile(request.getQrPic());
            event.setQrPic(qrPath);
        }

        // xử lý upload banner mới (nếu có)
        if (request.getPic() != null && !request.getPic().isEmpty()) {
            String picPath = localStorageService.uploadFile(request.getPic());
            event.setPic(picPath);
        }

        DonationEvent saved = donationEventRepository.save(event);


        Request req = Request.builder()
                .status(RequestStatus.PENDING)
                .requestType(ERequestType.DONATION_EDITION)
                .volunteer(event.getOrganization().getVolunteer())
                .organization(event.getOrganization())
                .donationEvent(saved)
                .build();

        Request savedRequest = requestService.createRequest(req);
        return toResponse(saved, BigDecimal.valueOf(0));
    }

    @Transactional
    public DonationEventListResponse updateDonationEventStatus(Long id, DonationEventStatusUpdateRequest request) {
        logger.info("Updating donation event status for id: {} to {}", id, request.getEventStatus());
        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));

        event.setEventStatus(request.getEventStatus());
        logger.info(String.valueOf(event.getEventStatus()));
        DonationEvent savedEvent = donationEventRepository.save(event);

        return toListResponse(savedEvent);
    }


    @Transactional
    public void deleteDonationEvent(Long id) {
        logger.info("Deleting donation event id: {}", id);
        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));
        donationEventRepository.delete(event);
    }

    private DonationEventResponse toResponse(DonationEvent event, BigDecimal totalDonated) {
        return new DonationEventResponse(
                event.getId(),
                event.getOrganization() != null ? event.getOrganization().getId() : null,
                event.getOrganization() != null ? event.getOrganization().getOrganizationName() : null,
                event.getTitle(),
                event.getDescription(),
                event.getMoneyNeed(),
                event.getEventStatus() != null ? event.getEventStatus().toString() : null,
                event.isHasDonate(),
                event.getNote(),
                event.getQrPic(),
                event.getBankAccount(),
                localStorageService.getFullFileUrl(event.getPic()),
                event.getDateStart(),
                event.getDateEnd(),
                totalDonated
        );
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
        response.setQrPic(localStorageService.getFullFileUrl(event.getQrPic()));
        response.setBankAccount(event.getBankAccount());
        response.setPic(localStorageService.getFullFileUrl(event.getPic()));
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
        response.setQrPic(localStorageService.getFullFileUrl(event.getQrPic()));
        response.setBankAccount(event.getBankAccount());
        response.setPic(localStorageService.getFullFileUrl(event.getPic()));
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