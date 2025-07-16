package com.volunteer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VolunteerDonationService {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerDonationService.class);

    public void getVolunteerDonations(Long volunteerId) {
        logger.info("Get donations for volunteer {}", volunteerId);
        // TODO: Implement retrieval logic
    }
} 