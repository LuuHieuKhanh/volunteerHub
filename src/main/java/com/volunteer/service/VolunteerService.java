package com.volunteer.service;

import com.volunteer.dto.donation.DonationEntryResponse;
import com.volunteer.dto.event.CharityEventResponse;
import com.volunteer.dto.organization.OrganizationResponse;
import com.volunteer.dto.volunteer.*;
import com.volunteer.entity.Account;
import com.volunteer.entity.Volunteer;
import com.volunteer.entity.VolunteerCharityEvent;
import com.volunteer.entity.VolunteerDonation;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.AccountRepository;
import com.volunteer.repository.VolunteerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolunteerService {

    private static final Logger logger = LoggerFactory.getLogger(VolunteerService.class);

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                volunteer.getAccount().getRole(),
                Optional.ofNullable(volunteer.getOrganization() != null ? volunteer.getOrganization().getId() : null)
        );
    }

    // ========== ADMIN METHODS ==========
    /**
     * Get all volunteers with search functionality (no pagination)
     */
    public List<VolunteerListResponse> getAllVolunteers(String search) {
        logger.info("Getting all volunteers with search: {}", search);

        List<Volunteer> volunteers;
        if (StringUtils.hasText(search)) {
            volunteers = volunteerRepository.findByFullNameContainingIgnoreCaseOrAccount_EmailContainingIgnoreCase(
                    search, search);
        } else {
            volunteers = volunteerRepository.findAll();
        }

        return volunteers.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new volunteer by admin
     */
    @Transactional
    public VolunteerListResponse createVolunteerByAdmin(VolunteerCreateRequest request) {
        logger.info("Creating volunteer by admin: {}", request.getEmail());

        // Check if email already exists
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        // Create Account
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole(request.getRole());
        account.setActive(true);
        Account savedAccount = accountRepository.save(account);

        // Create Volunteer
        Volunteer volunteer = new Volunteer();
        volunteer.setFullName(request.getFullName());
        volunteer.setContact(request.getContact());
        volunteer.setAccount(savedAccount);
        volunteer.setBanned(false);
        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        return toListResponse(savedVolunteer);
    }

    /**
     * Update volunteer by admin
     */
    @Transactional
    public VolunteerListResponse updateVolunteerByAdmin(Long id, VolunteerUpdateRequest request) {
        logger.info("Updating volunteer by admin id: {}", id);

        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));

        Account account = volunteer.getAccount();

        // Update volunteer fields
        if (StringUtils.hasText(request.getFullName())) {
            volunteer.setFullName(request.getFullName());
        }
        if (StringUtils.hasText(request.getContact())) {
            volunteer.setContact(request.getContact());
        }
        if (request.getIsBanned() != null) {
            volunteer.setBanned(request.getIsBanned());
        }

        // Update account fields
        if (StringUtils.hasText(request.getEmail())) {
            // Check if new email already exists
            if (!account.getEmail().equals(request.getEmail())
                    && accountRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists: " + request.getEmail());
            }
            account.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getPassword())) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null) {
            account.setRole(request.getRole());
        }
        if (request.getIsActive() != null) {
            account.setActive(request.getIsActive());
        }

        accountRepository.save(account);
        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        return toListResponse(savedVolunteer);
    }

    /**
     * Get volunteer detail with organization, events, and donations
     */
    public VolunteerDetailResponse getVolunteerDetail(Long id) {
        logger.info("Getting volunteer detail id: {}", id);

        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));

        VolunteerDetailResponse response = new VolunteerDetailResponse();
        response.setId(volunteer.getId());
        response.setFullName(volunteer.getFullName());
        response.setEmail(volunteer.getAccount().getEmail());
        response.setContact(volunteer.getContact());
        response.setPic(volunteer.getPic());
        response.setActive(volunteer.getAccount().isActive());
        response.setBanned(volunteer.isBanned());
        response.setRole(volunteer.getAccount().getRole());
        response.setCreatedAt(volunteer.getCreatedAt());
        response.setUpdatedAt(volunteer.getUpdatedAt());
        response.setDeletedAt(volunteer.getDeletedAt());
        response.setDeleted(volunteer.isDeleted());

        // Organization info
        if (volunteer.getOrganization() != null) {
            OrganizationResponse orgResponse = new OrganizationResponse(
                    volunteer.getOrganization().getId(),
                    volunteer.getOrganization().getOrganizationName(),
                    volunteer.getOrganization().getDescription(),
                    volunteer.getId()
            );
            response.setOrganization(orgResponse);
        }

        // Participated events
        List<CharityEventResponse> participatedEvents = volunteer.getJoinedEvents().stream()
                .map(vce -> {
                    CharityEventResponse eventResponse = new CharityEventResponse();
                    eventResponse.setId(vce.getCharityEvent().getId());
                    eventResponse.setCharityName(vce.getCharityEvent().getCharityName());
                    eventResponse.setDescription(vce.getCharityEvent().getDescription());
                    eventResponse.setDateStart(vce.getCharityEvent().getDateStart());
                    eventResponse.setDateEnd(vce.getCharityEvent().getDateEnd());
                    eventResponse.setEventStatus(vce.getCharityEvent().getEventStatus());
                    eventResponse.setJoinStatus(vce.getJoinStatus());
                    return eventResponse;
                })
                .collect(Collectors.toList());
        response.setParticipatedEvents(participatedEvents);

        // Donation history
        List<DonationEntryResponse> donationHistory = volunteer.getDonations().stream()
                .map(vd -> {
                    DonationEntryResponse donationResponse = new DonationEntryResponse();
                    donationResponse.setId(vd.getId());
                    donationResponse.setDonateAmount(vd.getDonateAmount());
                    donationResponse.setNote(vd.getNote());
                    donationResponse.setCreatedAt(vd.getCreatedAt());
                    donationResponse.setDonationEventTitle(vd.getDonationEvent().getTitle());
                    return donationResponse;
                })
                .collect(Collectors.toList());
        response.setDonationHistory(donationHistory);

        // Statistics
        response.setTotalEventsParticipated((long) participatedEvents.size());
        response.setTotalDonationCount((long) donationHistory.size());
        response.setTotalDonationAmount(donationHistory.stream()
                .map(DonationEntryResponse::getDonateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .longValue());

        return response;
    }

    /**
     * Soft delete volunteer by admin
     */
    @Transactional
    public void softDeleteVolunteerByAdmin(Long id) {
        logger.info("Soft deleting volunteer by admin id: {}", id);

        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));

        volunteer.setDeleted(true);
        volunteer.setDeletedAt(LocalDateTime.now());
        volunteer.getAccount().setActive(false);

        volunteerRepository.save(volunteer);
        accountRepository.save(volunteer.getAccount());
    }

    /**
     * Convert Volunteer to VolunteerListResponse
     */
    private VolunteerListResponse toListResponse(Volunteer volunteer) {
        VolunteerListResponse response = new VolunteerListResponse();
        response.setId(volunteer.getId());
        response.setFullName(volunteer.getFullName());
        response.setEmail(volunteer.getAccount().getEmail());
        response.setContact(volunteer.getContact());
        response.setPic(volunteer.getPic());
        response.setActive(volunteer.getAccount().isActive());
        response.setBanned(volunteer.isBanned());
        response.setRole(volunteer.getAccount().getRole());
        response.setCreatedAt(volunteer.getCreatedAt());
        response.setUpdatedAt(volunteer.getUpdatedAt());
        response.setDeleted(volunteer.isDeleted());

        if (volunteer.getOrganization() != null) {
            response.setOrganizationName(volunteer.getOrganization().getOrganizationName());
            response.setOrganizationId(volunteer.getOrganization().getId());
        }

        return response;
    }
}
