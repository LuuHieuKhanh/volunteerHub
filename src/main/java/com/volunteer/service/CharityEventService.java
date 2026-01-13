package com.volunteer.service;

import com.volunteer.dto.charity.*;
import com.volunteer.dto.event.CharityEventRequest;
import com.volunteer.dto.event.CharityEventResponse;
import com.volunteer.dto.event.CharityEventResponseList;
import com.volunteer.dto.event.VolunteerEventParticipationResponse;
import com.volunteer.entity.*;
import com.volunteer.enums.*;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private LocalStorageService localStorageService;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private RequestRepository requestRepository;

    @Autowired RequestService requestService;

    public List<CharityEventResponseList> getAllCharities(Long volunteerId, String search) {
        List<CharityEvent> events;

        if (search != null && !search.trim().isEmpty()) {
            events = charityEventRepository.findByCharityNameContainingIgnoreCaseOrOrganization_OrganizationNameContainingIgnoreCase(search, search);
        } else {
            events = charityEventRepository.findAll();
        }

        events = events.stream()
                .filter(e -> e.getEventStatus() != EEventStatus.PENDING && e.getEventStatus() != EEventStatus.CLOSED)
                .filter(e -> !e.getOrganization().isDeleted())
                .toList();

        return events.stream().map(event -> {
            boolean joined = volunteerCharityEventRepository
                    .existsByVolunteerIdAndCharityEventId(volunteerId, event.getId());

            boolean followed = followRepository
                    .existsByVolunteerIdAndOrganizationId(volunteerId, event.getOrganization().getId());

            return CharityEventResponseList.builder()
                    .id(event.getId())
                    .pic(localStorageService.getFullFileUrl(event.getPic()))
                    .name(event.getCharityName())
                    .description(event.getDescription())
                    .requirement(event.getRequirement())
                    .todo(event.getTodo())
                    .destination(event.getDestination())
                    .dateStart(event.getDateStart())
                    .numVolunteerRequire(event.getNumVolunteerRequire())
                    .numVolunteerActual(event.getNumVolunteerActual())
                    .status(event.getEventStatus())
                    .organization(CharityEventResponseList.OrganizationDto.builder()
                            .id(event.getOrganization().getId())
                            .name(event.getOrganization().getOrganizationName())
                            .avatar(
                                    Optional.ofNullable(event.getOrganization().getLogo())
                                            .map(localStorageService::getFullFileUrl)
                                            .orElse(null))
                            .build())
                    .joined(joined)
                    .followed(followed)
                    .build();
        }).toList();
    }

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
        event.setEventStatus(EEventStatus.PENDING);
        if (request.getPic() != null && !request.getPic().isEmpty()) {
            String filePath = localStorageService.uploadFile(request.getPic());
            event.setPic(filePath);
        }
        CharityEvent saved = charityEventRepository.save(event);


        Request req = Request.builder()
                .status(RequestStatus.PENDING)
                .requestType(ERequestType.CHARITY_REGISTRATION)
                .volunteer(org.getVolunteer())
                .organization(org)
                .charityEvent(saved)
                .build();

        Request savedRequest = requestService.createRequest(req);
        return toResponse(saved);
    }

    public List<CharityEventResponse> getCharitiesByOrganization(
            Long organizationId,
            String name,
            String from,
            String to
    ) {
        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (from != null && !from.isEmpty()) {
            fromDateTime = LocalDate.parse(from, formatter).atStartOfDay();
            // 2025-09-10T00:00:00
        }

        if (to != null && !to.isEmpty()) {
            toDateTime = LocalDate.parse(to, formatter).atTime(23, 59, 59);
            // 2025-09-10T23:59:59
        }

        List<CharityEvent> events = charityEventRepository.searchCharitiesByOrganization(
                organizationId, name, fromDateTime, toDateTime
        );

        return events.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void checkinVolunteer(Long eventId, Long volunteerId) {
        VolunteerCharityEvent vce = volunteerCharityEventRepository
                .findByVolunteerIdAndCharityEventId(volunteerId, eventId)
                .orElseThrow(() -> new RuntimeException("Volunteer chưa tham gia event này"));

        vce.setCheckin(!vce.isCheckin());
        volunteerCharityEventRepository.save(vce);
    }

    public List<VolunteerEventParticipationResponse> getVolunteersByCharity(Long charityId) {
        List<VolunteerCharityEvent> joined = volunteerCharityEventRepository.findByCharityEventId(charityId);

        return joined.stream().map(vce -> {
            Volunteer v = vce.getVolunteer();
            return VolunteerEventParticipationResponse.builder()
                    .id(v.getId())
                    .fullName(v.getFullName())
                    .contact(v.getContact())
                    .email(v.getAccount().getEmail())
                    .joinStatus(vce.getJoinStatus())
                    .joinDate(vce.getCreatedAt()) // 👈 lấy từ BaseEntity
                    .checkin(vce.isCheckin())
                    .build();
        }).toList();
    }

    public CharityEventResponseList getCharityById(Long charityId, Long volunteerId) {
        CharityEvent event = charityEventRepository.findById(charityId)
                .orElseThrow(() -> new RuntimeException("Charity event not found"));

        boolean joined = false;
        if (volunteerId != null) {
            joined = volunteerCharityEventRepository.existsByVolunteerIdAndCharityEventId(volunteerId, event.getId());
        }

        List<Request> requests = requestRepository.findCharityEventsByVolunteerAndRequestType(event.getOrganization().getId());
        logger.info("Requests found for volunteer {}: {}", volunteerId, requests);

        String denyReason = requests.stream()
                .map(Request::getDenyReason)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);


        return CharityEventResponseList.builder()
                .id(event.getId())
                .pic(localStorageService.getFullFileUrl(event.getPic()))
                .name(event.getCharityName())
                .description(event.getDescription())
                .requirement(event.getRequirement())
                .todo(event.getTodo())
                .destination(event.getDestination())
                .dateStart(event.getDateStart())
                .dateEnd(event.getDateEnd())
                .numVolunteerRequire(event.getNumVolunteerRequire())
                .numVolunteerActual(event.getNumVolunteerActual())
                .status(event.getEventStatus())
                .organization(CharityEventResponseList.OrganizationDto.builder()
                        .id(event.getOrganization().getId())
                        .name(event.getOrganization().getOrganizationName())
                        .avatar(Optional.ofNullable(event.getOrganization().getLogo())
                                .map(localStorageService::getFullFileUrl)
                                .orElse(null))
                        .reason(denyReason)
                        .build())
                .joined(joined)
                .build();
    }

    public void joinProgram(Long eventId, Long volunteerId) {
        CharityEvent event = charityEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if(event.getEventStatus() != EEventStatus.ACTIVE) {
            throw new RuntimeException("This program is not active");
        }

        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        // Check nếu đã join rồi
        if (volunteerCharityEventRepository.existsByVolunteerAndCharityEvent(volunteer, event)) {
            throw new RuntimeException("Already joined this program");
        }

        VolunteerCharityEvent vce = VolunteerCharityEvent.builder()
                .volunteer(volunteer)
                .charityEvent(event)
                .joinStatus(EJoinStatus.REGISTERED) // mặc định khi join
                .build();

        volunteerCharityEventRepository.save(vce);

        event.increaseVolunteerCount();
        charityEventRepository.save(event);
    }


    public void leaveProgram(Long eventId, Long volunteerId) {
        CharityEvent event = charityEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        VolunteerCharityEvent vce = volunteerCharityEventRepository
                .findByVolunteerAndCharityEvent(volunteer, event)
                .orElseThrow(() -> new RuntimeException("Not joined this program"));

        volunteerCharityEventRepository.delete(vce);

        event.decreaseVolunteerCount();
        charityEventRepository.save(event);
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

        Request req = Request.builder()
                .status(RequestStatus.PENDING)
                .requestType(ERequestType.CHARITY_EDITION)
                .volunteer(event.getOrganization().getVolunteer())
                .organization(event.getOrganization())
                .charityEvent(event)
                .build();

        Request savedRequest = requestService.createRequest(req);

        return toResponse(saved);
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
                event.getEventStatus()
        );
    }

    public CharityEventDetailResponse getCharityEventDetail(Long id) {
        logger.info("Getting charity event detail for id: {}", id);
        CharityEvent event = charityEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charity event not found with id: " + id));

        return toDetailResponse(event);
    }

    @Transactional
    public CharityEventListResponse createCharityEventAdmin(CharityEventCreateRequest request) {
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
        response.setPic(localStorageService.getFullFileUrl(event.getPic()));
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
        response.setPic(localStorageService.getFullFileUrl(event.getPic()));
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

    public List<VolunteerCharityEventHistoryResponse> getVolunteerCharityEventHistory(Long volunteerId, String search) {
        logger.info("Getting charity event history for volunteer id: {}", volunteerId);

        List<VolunteerCharityEvent> participations = volunteerCharityEventRepository.findByVolunteer_Id(volunteerId);

        return participations.stream()
                // ✅ Nếu có search thì lọc
                .filter(participation -> {
                    if (search == null || search.trim().isEmpty()) {
                        return true;
                    }
                    String keyword = search.trim().toLowerCase();
                    CharityEvent event = participation.getCharityEvent();
                    if (event == null) return false;

                    boolean matchEventName = event.getCharityName() != null &&
                            event.getCharityName().toLowerCase().contains(keyword);
                    boolean matchDescription = event.getDescription() != null &&
                            event.getDescription().toLowerCase().contains(keyword);
                    boolean matchOrg = event.getOrganization() != null &&
                            event.getOrganization().getOrganizationName() != null &&
                            event.getOrganization().getOrganizationName().toLowerCase().contains(keyword);

                    return matchEventName || matchDescription || matchOrg;
                })
                // ✅ Map sang response
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
                        response.setPic(localStorageService.getFullFileUrl(event.getPic()));
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
}
