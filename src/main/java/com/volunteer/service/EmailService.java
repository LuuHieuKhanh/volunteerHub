package com.volunteer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendRequestStatusUpdateEmail(String to, String status) {
        logger.info("Send request status update email to {} with status {}", to, status);
        // TODO: Implement email sending logic
    }

    public void sendParticipationStatusUpdateEmail(String to, String status) {
        logger.info("Send participation status update email to {} with status {}", to, status);
        // TODO: Implement email sending logic
    }
} 