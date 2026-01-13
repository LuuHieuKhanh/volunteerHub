package com.volunteer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private JavaMailSender mailSender;

    public void sendNewPasswordEmail(String toEmail, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("VolunteerHub sent you a new password");
        message.setText(buildEmailContent(newPassword));

        mailSender.send(message);
    }

    private String buildEmailContent(String newPassword) {
        return "Dear User,\n\n" +
                "You have requested a new password for your VolunteerHub account.\n\n" +
                "Your new password is: " + newPassword + "\n\n" +
                "Please log in with this new password and consider changing it to something more secure after your first login.\n\n" +
                "If you did not request this password reset, please contact our support team immediately.\n\n" +
                "Best regards,\n" +
                "VolunteerHub Team";
    }
} 