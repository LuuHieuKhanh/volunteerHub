package com.volunteer.service;

import com.volunteer.dto.charity.*;
import com.volunteer.dto.event.*;
import com.volunteer.entity.CharityEvent;
import com.volunteer.entity.Organization;
import com.volunteer.entity.Volunteer;
import com.volunteer.entity.VolunteerCharityEvent;
import com.volunteer.enums.EEventStatus;
import com.volunteer.enums.EJoinStatus;
import com.volunteer.enums.RequestStatus;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.CharityEventRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.RequestRepository;
import com.volunteer.repository.VolunteerCharityEventRepository;
import com.volunteer.repository.VolunteerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharityEventService {

    private static final Logger logger = LoggerFactory.getLogger(CharityEventService.class);

    @Autowired
    private CharityEventRepository charityEventRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private VolunteerCharityEventRepository volunteerCharityEventRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<CharityEventListResponse> getAllCharityEvents(String search) {
        logger.info("Getting all charity events with search: {}", search);
        List<CharityEvent> events;

        if (StringUtils.hasText(search)) {
            events = charityEventRepository.findByCharityNameContainingIgnoreCaseOrOrganization_OrganizationNameContainingIgnoreCase(search, search);
        } else {
            events = charityEventRepository.findAll();
        }

        return events.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    public CharityEventDetailResponse getCharityEventDetail(Long id) {
        logger.info("Getting charity event detail for id: {}", id);
        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));

        return toDetailResponse(event);
    }

    @Transactional
    public CharityEventListResponse createCharityEvent(CharityEventCreateRequest request) {
        logger.info("Creating charity event: {}", request.getCharityName());

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));

        CharityEvent event = CharityEvent.builder()
                .organization(organization)
                .charityName(request.getCharityName())
                .description(request.getDescription())
                .todo(request.getTodo())
                .requirement(request.getRequirement())
                .destination(request.getDestination())
                .dateStart(request.getDateStart())
                .dateEnd(request.getDateEnd())
                .numVolunteerRequire(request.getNumVolunteerRequire())
                .numVolunteerActual(0L)
                .note(request.getNote())
                .pic(request.getPic())
                .eventStatus(request.getEventStatus())
                .build();

        CharityEvent savedEvent = charityEventRepository.save(event);
        return toListResponse(savedEvent);
    }

    @Transactional
    public CharityEventListResponse updateCharityEvent(Long id, CharityEventUpdateRequest request) {
        logger.info("Updating charity event id: {}", id);

        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));

        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));
            event.setOrganization(organization);
        }

        if (request.getCharityName() != null) {
            event.setCharityName(request.getCharityName());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getTodo() != null) {
            event.setTodo(request.getTodo());
        }
        if (request.getRequirement() != null) {
            event.setRequirement(request.getRequirement());
        }
        if (request.getDestination() != null) {
            event.setDestination(request.getDestination());
        }
        if (request.getDateStart() != null) {
            event.setDateStart(request.getDateStart());
        }
        if (request.getDateEnd() != null) {
            event.setDateEnd(request.getDateEnd());
        }
        if (request.getNumVolunteerRequire() != null) {
            event.setNumVolunteerRequire(request.getNumVolunteerRequire());
        }
        if (request.getNote() != null) {
            event.setNote(request.getNote());
        }
        if (request.getPic() != null) {
            event.setPic(request.getPic());
        }
        if (request.getEventStatus() != null) {
            event.setEventStatus(request.getEventStatus());
        }

        CharityEvent savedEvent = charityEventRepository.save(event);
        return toListResponse(savedEvent);
    }

    @Transactional
    public void softDeleteCharityEvent(Long id) {
        logger.info("Soft deleting charity event id: {}", id);
        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));

        event.setDeleted(true);
        event.setDeletedAt(LocalDateTime.now());
        charityEventRepository.save(event);
    }

    @Transactional
    public CharityEventListResponse updateCharityEventStatus(Long id, CharityEventStatusUpdateRequest request) {
        logger.info("Updating charity event status for id: {} to {}", id, request.getEventStatus());

        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));

        event.setEventStatus(request.getEventStatus());
        CharityEvent savedEvent = charityEventRepository.save(event);

        return toListResponse(savedEvent);
    }

    private CharityEventListResponse toListResponse(CharityEvent event) {
        CharityEventListResponse response = new CharityEventListResponse();
        response.setId(event.getId());
        response.setCharityName(event.getCharityName());
        response.setDescription(event.getDescription());
        response.setTodo(event.getTodo());
        response.setRequirement(event.getRequirement());
        response.setDestination(event.getDestination());
        response.setDateStart(event.getDateStart());
        response.setDateEnd(event.getDateEnd());
        response.setNumVolunteerRequire(event.getNumVolunteerRequire());
        response.setNumVolunteerActual(event.getNumVolunteerActual());
        response.setNote(event.getNote());
        response.setPic(event.getPic());
        response.setEventStatus(event.getEventStatus());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());

        // Organization info
        if (event.getOrganization() != null) {
            Organization org = event.getOrganization();
            response.setOrganizationId(org.getId());
            response.setOrganizationName(org.getOrganizationName());
            response.setOrganizationDescription(org.getDescription());
        }

        // Calculate total participants count
        List<VolunteerCharityEvent> participants = volunteerCharityEventRepository.findByCharityEventId(event.getId());
        response.setTotalParticipants((long) participants.size());

        return response;
    }

    private CharityEventDetailResponse toDetailResponse(CharityEvent event) {
        CharityEventDetailResponse response = new CharityEventDetailResponse();
        response.setId(event.getId());
        response.setCharityName(event.getCharityName());
        response.setDescription(event.getDescription());
        response.setTodo(event.getTodo());
        response.setRequirement(event.getRequirement());
        response.setDestination(event.getDestination());
        response.setDateStart(event.getDateStart());
        response.setDateEnd(event.getDateEnd());
        response.setNumVolunteerRequire(event.getNumVolunteerRequire());
        response.setNumVolunteerActual(event.getNumVolunteerActual());
        response.setNote(event.getNote());
        response.setPic(event.getPic());
        response.setEventStatus(event.getEventStatus());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());

        // Organization info
        if (event.getOrganization() != null) {
            Organization org = event.getOrganization();
            response.setOrganizationId(org.getId());
            response.setOrganizationName(org.getOrganizationName());
            response.setOrganizationDescription(org.getDescription());
        }

        // Get participants list
        List<VolunteerCharityEvent> participants = volunteerCharityEventRepository.findByCharityEventId(event.getId());
        response.setTotalParticipants((long) participants.size());

        // Map participants list
        List<ParticipantResponse> participantResponses = participants.stream()
                .map(participation -> {
                    ParticipantResponse participantResponse = new ParticipantResponse();
                    participantResponse.setId(participation.getId());
                    participantResponse.setJoinStatus(participation.getJoinStatus());
                    participantResponse.setCreatedAt(participation.getCreatedAt());
                    participantResponse.setUpdatedAt(participation.getUpdatedAt());

                    if (participation.getVolunteer() != null) {
                        participantResponse.setVolunteerId(participation.getVolunteer().getId());
                        participantResponse.setVolunteerFullName(participation.getVolunteer().getFullName());
                        participantResponse.setVolunteerContact(participation.getVolunteer().getContact());
                        if (participation.getVolunteer().getAccount() != null) {
                            participantResponse.setVolunteerEmail(participation.getVolunteer().getAccount().getEmail());
                        }
                    }

                    return participantResponse;
                })
                .collect(Collectors.toList());
        response.setParticipants(participantResponses);

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

    public List<VolunteerCharityEventHistoryResponse> getVolunteerCharityEventHistory(Long volunteerId) {
        logger.info("Getting charity event history for volunteer id: {}", volunteerId);

        List<VolunteerCharityEvent> participations = volunteerCharityEventRepository.findByVolunteer_Id(volunteerId);

        return participations.stream()
                .map(participation -> {
                    VolunteerCharityEventHistoryResponse response = new VolunteerCharityEventHistoryResponse();

                    // Participation info
                    response.setId(participation.getId());
                    response.setJoinStatus(participation.getJoinStatus());
                    response.setParticipationCreatedAt(participation.getCreatedAt());
                    response.setParticipationUpdatedAt(participation.getUpdatedAt());

                    // Charity event info
                    if (participation.getCharityEvent() != null) {
                        CharityEvent event = participation.getCharityEvent();
                        response.setCharityEventId(event.getId());
                        response.setCharityEventName(event.getCharityName());
                        response.setCharityEventDescription(event.getDescription());
                        response.setTodo(event.getTodo());
                        response.setRequirement(event.getRequirement());
                        response.setDestination(event.getDestination());
                        response.setDateStart(event.getDateStart());
                        response.setDateEnd(event.getDateEnd());
                        response.setNumVolunteerRequire(event.getNumVolunteerRequire());
                        response.setNumVolunteerActual(event.getNumVolunteerActual());
                        response.setNote(event.getNote());
                        response.setPic(event.getPic());
                        response.setEventStatus(event.getEventStatus());
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

    // ==================== EXISTING METHODS FOR EVENTCONTROLLER ====================
    public List<CharityEventResponseList> getAllCharities(Long volunteerId) {
        logger.info("Getting all charities for volunteer: {}", volunteerId);
        List<CharityEvent> events = charityEventRepository.findAll();

        return events.stream()
                .map(event -> {
                    CharityEventResponseList response = CharityEventResponseList.builder()
                            .id(event.getId())
                            .pic(event.getPic())
                            .name(event.getCharityName())
                            .description(event.getDescription())
                            .requirement(event.getRequirement())
                            .todo(event.getTodo())
                            .destination(event.getDestination())
                            .dateStart(event.getDateStart())
                            .numVolunteerRequire(event.getNumVolunteerRequire())
                            .numVolunteerActual(event.getNumVolunteerActual())
                            .build();

                    // Organization info
                    if (event.getOrganization() != null) {
                        Organization org = event.getOrganization();
                        CharityEventResponseList.OrganizationDto orgDto = CharityEventResponseList.OrganizationDto.builder()
                                .id(org.getId())
                                .name(org.getOrganizationName())
                                .avatar(org.getLogo())
                                .build();
                        response.setOrganization(orgDto);
                    }

                    // Check if volunteer has joined
                    if (volunteerId != null) {
                        boolean joined = volunteerCharityEventRepository.existsByVolunteerIdAndCharityEventId(volunteerId, event.getId());
                        response.setJoined(joined);
                    } else {
                        response.setJoined(false);
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public CharityEventResponse createCharityEvent(CharityEventRequest request) throws IOException {
        logger.info("Creating charity event: {}", request.getCharityName());

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));

        CharityEvent event = CharityEvent.builder()
                .organization(organization)
                .charityName(request.getCharityName())
                .description(request.getDescription())
                .todo(request.getTodo())
                .requirement(request.getRequirement())
                .destination(request.getDestination())
                .dateStart(request.getDateStart())
                .dateEnd(request.getDateEnd())
                .numVolunteerRequire(request.getNumVolunteerRequire())
                .numVolunteerActual(0L)
                .note(request.getNote())
                .eventStatus(EEventStatus.UPCOMING)
                .build();

        // Handle file upload if present
        if (request.getPic() != null && !request.getPic().isEmpty()) {
            // TODO: Implement file upload logic
            event.setPic("uploaded_image_url");
        }

        CharityEvent savedEvent = charityEventRepository.save(event);
        return toEventResponse(savedEvent);
    }

    @Transactional
    public void joinProgram(Long eventId, Long volunteerId) {
        logger.info("Volunteer {} joining charity event {}", volunteerId, eventId);

        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + volunteerId));

        CharityEvent event = charityEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + eventId));

        // Check if already joined
        if (volunteerCharityEventRepository.existsByVolunteerAndCharityEvent(volunteer, event)) {
            throw new RuntimeException("Volunteer has already joined this event");
        }

        // Check if event is full
        if (event.getNumVolunteerActual() >= event.getNumVolunteerRequire()) {
            throw new RuntimeException("Event is full");
        }

        VolunteerCharityEvent participation = VolunteerCharityEvent.builder()
                .volunteer(volunteer)
                .charityEvent(event)
                .joinStatus(EJoinStatus.REGISTERED)
                .build();

        volunteerCharityEventRepository.save(participation);
        event.increaseVolunteerCount();
        charityEventRepository.save(event);
    }

    @Transactional
    public void leaveProgram(Long eventId, Long volunteerId) {
        logger.info("Volunteer {} leaving charity event {}", volunteerId, eventId);

        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + volunteerId));

        CharityEvent event = charityEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + eventId));

        VolunteerCharityEvent participation = volunteerCharityEventRepository.findByVolunteerAndCharityEvent(volunteer, event)
                .orElseThrow(() -> new ResourceNotFoundException("Participation not found"));

        volunteerCharityEventRepository.delete(participation);
        event.decreaseVolunteerCount();
        charityEventRepository.save(event);
    }

    public List<CharityEventResponse> getCharitiesByOrganization(Long organizationId, String name, String from, String to) {
        logger.info("Getting charities by organization: {}", organizationId);

        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        if (from != null && !from.isEmpty()) {
            fromDate = LocalDateTime.parse(from);
        }
        if (to != null && !to.isEmpty()) {
            toDate = LocalDateTime.parse(to);
        }

        List<CharityEvent> events = charityEventRepository.searchCharitiesByOrganization(organizationId, name, fromDate, toDate);

        return events.stream()
                .map(this::toEventResponse)
                .collect(Collectors.toList());
    }

    public CharityEventResponseList getCharityById(Long charityId, Long volunteerId) {
        logger.info("Getting charity by id: {} for volunteer: {}", charityId, volunteerId);

        CharityEvent event = charityEventRepository.findById(charityId)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + charityId));

        CharityEventResponseList response = CharityEventResponseList.builder()
                .id(event.getId())
                .pic(event.getPic())
                .name(event.getCharityName())
                .description(event.getDescription())
                .requirement(event.getRequirement())
                .todo(event.getTodo())
                .destination(event.getDestination())
                .dateStart(event.getDateStart())
                .numVolunteerRequire(event.getNumVolunteerRequire())
                .numVolunteerActual(event.getNumVolunteerActual())
                .build();

        // Organization info
        if (event.getOrganization() != null) {
            Organization org = event.getOrganization();
            CharityEventResponseList.OrganizationDto orgDto = CharityEventResponseList.OrganizationDto.builder()
                    .id(org.getId())
                    .name(org.getOrganizationName())
                    .avatar(org.getLogo())
                    .build();
            response.setOrganization(orgDto);
        }

        // Check if volunteer has joined
        if (volunteerId != null) {
            boolean joined = volunteerCharityEventRepository.existsByVolunteerIdAndCharityEventId(volunteerId, event.getId());
            response.setJoined(joined);
        } else {
            response.setJoined(false);
        }

        return response;
    }

    @Transactional
    public CharityEventResponse updateCharityEvent(Long id, CharityEventRequest request) throws IOException {
        logger.info("Updating charity event id: {}", id);

        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));

        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrganizationId()));
            event.setOrganization(organization);
        }

        if (request.getCharityName() != null) {
            event.setCharityName(request.getCharityName());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getTodo() != null) {
            event.setTodo(request.getTodo());
        }
        if (request.getRequirement() != null) {
            event.setRequirement(request.getRequirement());
        }
        if (request.getDestination() != null) {
            event.setDestination(request.getDestination());
        }
        if (request.getDateStart() != null) {
            event.setDateStart(request.getDateStart());
        }
        if (request.getDateEnd() != null) {
            event.setDateEnd(request.getDateEnd());
        }
        if (request.getNumVolunteerRequire() != null) {
            event.setNumVolunteerRequire(request.getNumVolunteerRequire());
        }
        if (request.getNote() != null) {
            event.setNote(request.getNote());
        }

        // Handle file upload if present
        if (request.getPic() != null && !request.getPic().isEmpty()) {
            // TODO: Implement file upload logic
            event.setPic("updated_image_url");
        }

        CharityEvent savedEvent = charityEventRepository.save(event);
        return toEventResponse(savedEvent);
    }

    @Transactional
    public void deleteCharityEvent(Long id) {
        logger.info("Deleting charity event id: {}", id);
        softDeleteCharityEvent(id);
    }

    private CharityEventResponse toEventResponse(CharityEvent event) {
        Long organizationId = event.getOrganization() != null ? event.getOrganization().getId() : null;

        CharityEventResponse response = new CharityEventResponse(
                event.getId(),
                event.getCharityName(),
                organizationId,
                event.getDescription(),
                event.getTodo(),
                event.getRequirement(),
                event.getDestination(),
                event.getDateStart(),
                event.getDateEnd(),
                event.getNumVolunteerRequire(),
                event.getNumVolunteerActual(),
                event.getNote(),
                event.getPic(),
                event.getEventStatus()
        );

        return response;
    }
}
