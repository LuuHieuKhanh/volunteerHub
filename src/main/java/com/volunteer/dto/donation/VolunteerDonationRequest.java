package com.volunteer.dto.donation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VolunteerDonationRequest {
    private Long volunteerId;
    private Long donationEventId;
    private BigDecimal donateAmount;
    private String note;
}
