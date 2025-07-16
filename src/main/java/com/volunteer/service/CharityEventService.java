package com.volunteer.service;

import com.volunteer.dto.event.CharityEventRequest;
import com.volunteer.dto.event.CharityEventResponse;
import com.volunteer.entity.CharityEvent;
import com.volunteer.entity.Organization;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.CharityEventRepository;
import com.volunteer.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CharityEventService {
    private static final Logger logger = LoggerFactory.getLogger(CharityEventService.class);

    @Autowired
    private CharityEventRepository charityEventRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Transactional
    public CharityEventResponse createCharityEvent(CharityEventRequest request) {
        logger.info("Creating charity event: {}", request.getCharityName());
        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));
        CharityEvent event = new CharityEvent();
        event.setCharityName(request.getCharityName());
        event.setOrganization(org);
        event.setDescription(request.getDescription());
        event.setDestination(request.getDestination());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());
        event.setNumVolunteerRequire(request.getNumVolunteerRequire());
        event.setNote(request.getNote());
        event.setPic(request.getPic());
        CharityEvent saved = charityEventRepository.save(event);
        return toResponse(saved);
    }

    public CharityEventResponse getCharityEventById(Long id) {
        logger.info("Fetching charity event by id: {}", id);
        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));
        return toResponse(event);
    }

    @Transactional
    public CharityEventResponse updateCharityEvent(Long id, CharityEventRequest request) {
        logger.info("Updating charity event id: {}", id);
        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));
        event.setCharityName(request.getCharityName());
        event.setDescription(request.getDescription());
        event.setDestination(request.getDestination());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());
        event.setNumVolunteerRequire(request.getNumVolunteerRequire());
        event.setNote(request.getNote());
        event.setPic(request.getPic());
        CharityEvent saved = charityEventRepository.save(event);
        return toResponse(saved);
    }

    @Transactional
    public void deleteCharityEvent(Long id) {
        logger.info("Deleting charity event id: {}", id);
        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));
        charityEventRepository.delete(event);
    }

    private CharityEventResponse toResponse(CharityEvent event) {
        return new CharityEventResponse(
                event.getId(),
                event.getCharityName(),
                event.getOrganization() != null ? event.getOrganization().getId() : null,
                event.getDescription(),
                event.getDestination(),
                event.getDateStart(),
                event.getDateEnd(),
                event.getNumVolunteerRequire(),
                event.getNumVolunteerActual(),
                event.getNote(),
                event.getPic(),
                event.getEventStatus() != null ? event.getEventStatus().toString() : null
        );
    }
} 