package com.volunteer.service;

import com.volunteer.dto.volunteer.VolunteerResponse;
import com.volunteer.entity.Volunteer;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.VolunteerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VolunteerService {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerService.class);

    @Autowired
    private VolunteerRepository volunteerRepository;

    public VolunteerResponse createVolunteer(Volunteer volunteer) {
        Volunteer saved = volunteerRepository.save(volunteer);
        return toResponse(saved);
    }

    public VolunteerResponse getVolunteerByEmail(String email, boolean isActive) {
        logger.info("getVolunteerByEmail");
        Optional<Volunteer> volunteer = volunteerRepository.findByAccount_EmailAndAccount_IsActive(email, isActive);
        return volunteer
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with email: " + email));
    }

    public VolunteerResponse getVolunteerById(Long id) {
        logger.info("Fetching volunteer by id: {}", id);
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
        return toResponse(volunteer);
    }

    public VolunteerResponse updateVolunteer(Long id, Volunteer updated) {
        logger.info("Updating volunteer id: {}", id);
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
        volunteer.setFullName(updated.getFullName());
        volunteer.setPic(updated.getPic());
        volunteer.setContact(updated.getContact());
        Volunteer saved = volunteerRepository.save(volunteer);
        return toResponse(saved);
    }

    @Transactional
    public void softDeleteCurrentUserAccount(Long id) {
        logger.info("Soft delete current user account id: {}", id);
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
        volunteer.setDeleted(true);
        volunteer.setDeletedAt(LocalDateTime.now());
        volunteerRepository.save(volunteer);
    }

    @Transactional
    public void hardDeleteVolunteerAccount(Long id) {
        logger.info("Hard delete volunteer account id: {}", id);
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
        volunteerRepository.delete(volunteer);
    }

    public VolunteerResponse toResponse(Volunteer volunteer) {
        return new VolunteerResponse(
                volunteer.getId(),
                volunteer.getFullName(),
                volunteer.getAccount() != null ? volunteer.getAccount().getEmail() : null,
                volunteer.getPic(),
                volunteer.getContact(),
                volunteer.getAccount() != null ? volunteer.getAccount().getId() : null,
                volunteer.getAccount() != null && volunteer.getAccount().isActive(),
                volunteer.getDeletedAt(),
                volunteer.getAccount().getRole()
        );
    }
} 