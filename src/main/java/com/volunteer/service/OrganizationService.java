package com.volunteer.service;

import com.volunteer.dto.organization.OrganizationRequest;
import com.volunteer.dto.organization.OrganizationResponse;
import com.volunteer.entity.Organization;
import com.volunteer.entity.Volunteer;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.VolunteerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;

    @Transactional
    public Organization createOrganization(OrganizationRequest request) {
        logger.info("Creating organization: {}", request.getOrganizationName());
        Volunteer volunteer = volunteerRepository.findById(request.getVolunteerId())
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + request.getVolunteerId()));
        Organization org = new Organization();
        org.setOrganizationName(request.getOrganizationName());
        org.setDescription(request.getDescription());
        org.setLogo(request.getLogo());
        org.setCertificate(request.getCertificate());
        org.setVolunteer(volunteer);
        Organization saved = organizationRepository.save(org);
        return saved;
    }

    public OrganizationResponse getOrganizationById(Long id) {
        logger.info("Fetching organization by id: {}", id);
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        return toResponse(org);
    }

    @Transactional
    public OrganizationResponse updateOrganization(Long id, OrganizationRequest request) {
        logger.info("Updating organization id: {}", id);
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        org.setOrganizationName(request.getOrganizationName());
        org.setDescription(request.getDescription());
        Organization saved = organizationRepository.save(org);
        return toResponse(saved);
    }

    @Transactional
    public void deleteOrganization(Long id) {
        logger.info("Deleting organization id: {}", id);
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        organizationRepository.delete(org);
    }

    private OrganizationResponse toResponse(Organization org) {
        return new OrganizationResponse(
                org.getId(),
                org.getOrganizationName(),
                org.getDescription(),
                org.getVolunteer() != null ? org.getVolunteer().getId() : null
        );
    }
} 