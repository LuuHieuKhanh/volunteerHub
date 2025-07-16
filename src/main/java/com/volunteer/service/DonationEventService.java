package com.volunteer.service;

import com.volunteer.dto.event.DonationEventRequest;
import com.volunteer.dto.event.DonationEventResponse;
import com.volunteer.entity.DonationEvent;
import com.volunteer.entity.Organization;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.DonationEventRepository;
import com.volunteer.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationEventService {
    private static final Logger logger = LoggerFactory.getLogger(DonationEventService.class);

    @Autowired
    private DonationEventRepository donationEventRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Transactional
    public DonationEventResponse createDonationEvent(DonationEventRequest request) {
        logger.info("Creating donation event: {}", request.getTitle());
        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));
        DonationEvent event = new DonationEvent();
        event.setOrganization(org);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setMoneyNeed(request.getMoneyNeed());
        event.setNote(request.getNote());
        event.setQrPic(request.getQrPic());
        event.setBankAccount(request.getBankAccount());
        event.setPic(request.getPic());
        DonationEvent saved = donationEventRepository.save(event);
        return toResponse(saved);
    }

    public DonationEventResponse getDonationEventById(Long id) {
        logger.info("Fetching donation event by id: {}", id);
        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));
        return toResponse(event);
    }

    @Transactional
    public DonationEventResponse updateDonationEvent(Long id, DonationEventRequest request) {
        logger.info("Updating donation event id: {}", id);
        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setMoneyNeed(request.getMoneyNeed());
        event.setNote(request.getNote());
        event.setQrPic(request.getQrPic());
        event.setBankAccount(request.getBankAccount());
        event.setPic(request.getPic());
        DonationEvent saved = donationEventRepository.save(event);
        return toResponse(saved);
    }

    @Transactional
    public void deleteDonationEvent(Long id) {
        logger.info("Deleting donation event id: {}", id);
        DonationEvent event = donationEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation event not found with id: " + id));
        donationEventRepository.delete(event);
    }

    private DonationEventResponse toResponse(DonationEvent event) {
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
                event.getPic()
        );
    }
} 