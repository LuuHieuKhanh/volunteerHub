package com.volunteer.service;

import com.volunteer.dto.event.CharityEventRequest;
import com.volunteer.dto.event.CharityEventResponse;
import com.volunteer.dto.event.CharityEventResponseList;
import com.volunteer.entity.CharityEvent;
import com.volunteer.entity.Organization;
import com.volunteer.entity.Volunteer;
import com.volunteer.entity.VolunteerCharityEvent;
import com.volunteer.enums.EJoinStatus;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.CharityEventRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.VolunteerCharityEventRepository;
import com.volunteer.repository.VolunteerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private VolunteerCharityEventRepository volunteerCharityEventRepository;
    @Autowired
    private LocalStorageService localStorageService;
    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<CharityEventResponseList> getAllCharities(Long volunteerId) {
        List<CharityEvent> events = charityEventRepository.findAll();

        return events.stream().map(event -> {
            boolean joined = volunteerCharityEventRepository
                    .existsByVolunteerIdAndCharityEventId(volunteerId, event.getId());

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
                    .organization(CharityEventResponseList.OrganizationDto.builder()
                            .id(event.getOrganization().getId())
                            .name(event.getOrganization().getOrganizationName())
                            .avatar(
                                    Optional.ofNullable(event.getOrganization().getLogo())
                                            .map(localStorageService::getFullFileUrl)
                                            .orElse(null))
                            .build())
                    .joined(joined)
                    .build();
        }).toList();
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
        if (request.getPic() != null && !request.getPic().isEmpty()) {
            String filePath = localStorageService.uploadFile(request.getPic());
            event.setPic(filePath);
        }
        CharityEvent saved = charityEventRepository.save(event);
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

    public CharityEventResponseList getCharityById(Long charityId, Long volunteerId) {
        CharityEvent event = charityEventRepository.findById(charityId)
                .orElseThrow(() -> new RuntimeException("Charity event not found"));

        boolean joined = false;
        if (volunteerId != null) {
            joined = volunteerCharityEventRepository.existsByVolunteerIdAndCharityEventId(volunteerId, event.getId());
        }

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
                .organization(CharityEventResponseList.OrganizationDto.builder()
                        .id(event.getOrganization().getId())
                        .name(event.getOrganization().getOrganizationName())
                        .avatar(Optional.ofNullable(event.getOrganization().getLogo())
                                .map(localStorageService::getFullFileUrl)
                                .orElse(null))
                        .build())
                .joined(joined)
                .build();
    }

    public void joinProgram(Long eventId, Long volunteerId) {
        CharityEvent event = charityEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

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
                event.getEventStatus()
        );
    }
}
