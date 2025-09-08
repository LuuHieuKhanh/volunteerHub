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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CharityEventService {
    private static final Logger logger = LoggerFactory.getLogger(CharityEventService.class);

    @Autowired
    private CharityEventRepository charityEventRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private LocalStorageService localStorageService;

    @Transactional
    public CharityEventResponse createCharityEvent(CharityEventRequest request) throws IOException {
        logger.info("Creating charity event: {}", request.getCharityName());
        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));
        CharityEvent event = new CharityEvent();
        event.setCharityName(request.getCharityName());
        event.setOrganization(org);
        event.setDescription(request.getDescription());
        event.setDestination(request.getDestination());
        event.setTodo(request.getTodo());
        event.setRequirement(request.getRequirement());
        event.setDateStart(request.getDateStart());
        event.setDateEnd(request.getDateEnd());
        event.setNumVolunteerRequire(request.getNumVolunteerRequire());
        event.setNote(request.getNote());
        if (request.getPic() != null && !request.getPic().isEmpty()) {
            String filePath = localStorageService.uploadFile(request.getPic());
            event.setPic(filePath);
        }
        CharityEvent saved = charityEventRepository.save(event);
        return toResponse(saved);
    }

    public List<CharityEventResponse> getCharitiesByOrganization(Long organizationId) {
        List<CharityEvent> events = charityEventRepository.findByOrganization_Id(organizationId);
        return events.stream()
                .map(this::toResponse)
                .toList();
    }

    public CharityEventResponse getCharityEventById(Long id) {
        logger.info("Fetching charity event by id: {}", id);
        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));
        return toResponse(event);
    }

    @Transactional
    public CharityEventResponse updateCharityEvent(Long id, CharityEventRequest request) throws IOException {
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
        if (request.getPic() != null && !request.getPic().isEmpty()) {
            String filePath = localStorageService.uploadFile(request.getPic());
            event.setPic(filePath);
        }
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
                localStorageService.getFullFileUrl(event.getPic()),
                event.getEventStatus() != null ? event.getEventStatus().toString() : null
        );
    }
} 