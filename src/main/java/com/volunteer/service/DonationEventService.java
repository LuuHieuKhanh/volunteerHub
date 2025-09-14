package com.volunteer.service;

import com.volunteer.dto.event.DonationEventRequest;
import com.volunteer.dto.event.DonationEventResponse;
import com.volunteer.dto.event.VolunteerDonationResponse;
import com.volunteer.entity.DonationEvent;
import com.volunteer.entity.Organization;
import com.volunteer.entity.Volunteer;
import com.volunteer.entity.VolunteerDonation;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.DonationEventRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.VolunteerDonationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        return toResponse(saved, BigDecimal.valueOf(0));
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
} 