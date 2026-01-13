package com.volunteer.dto.donation;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DonorResponse {

    private Long id;
    private Long volunteerId;
    private String volunteerFullName;
    private String volunteerContact;
    private String volunteerEmail;
    private BigDecimal donateAmount;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}