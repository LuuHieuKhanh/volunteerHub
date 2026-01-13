package com.volunteer.service;

import com.volunteer.dto.donation.VolunteerDonationRequest;
import com.volunteer.entity.DonationEvent;
import com.volunteer.entity.Volunteer;
import com.volunteer.entity.VolunteerDonation;
import com.volunteer.repository.DonationEventRepository;
import com.volunteer.repository.VolunteerDonationRepository;
import com.volunteer.repository.VolunteerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VolunteerDonationService {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerDonationService.class);

    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private DonationEventRepository donationEventRepository;
    @Autowired
    private VolunteerDonationRepository volunteerDonationRepository;
    public void getVolunteerDonations(Long volunteerId) {
        logger.info("Get donations for volunteer {}", volunteerId);
        // TODO: Implement retrieval logic
    }

    @Transactional
    public VolunteerDonation createDonation(VolunteerDonationRequest request) {
        Volunteer volunteer = volunteerRepository.findById(request.getVolunteerId())
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        DonationEvent donationEvent = donationEventRepository.findById(request.getDonationEventId())
                .orElseThrow(() -> new RuntimeException("Donation event not found"));

        VolunteerDonation donation = VolunteerDonation.builder()
                .volunteer(volunteer)
                .donationEvent(donationEvent)
                .donateAmount(request.getDonateAmount())
                .note(request.getNote())
                .build();

        return volunteerDonationRepository.save(donation);
    }
} 