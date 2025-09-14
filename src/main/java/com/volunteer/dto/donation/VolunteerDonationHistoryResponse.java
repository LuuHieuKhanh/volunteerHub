package com.volunteer.dto.donation;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VolunteerDonationHistoryResponse {

    private Long id;
    private Long donationEventId;
    private String donationEventTitle;
    private String donationEventDescription;
    private BigDecimal moneyNeed;
    private EEventStatus eventStatus;
    private String note;
    private String qrPic;
    private String bankAccount;
    private String pic;
    private LocalDateTime eventCreatedAt;
    private LocalDateTime eventUpdatedAt;

    // Organization info
    private Long organizationId;
    private String organizationName;
    private String organizationDescription;

    // Donation info
    private BigDecimal donateAmount;
    private String donationNote;
    private LocalDateTime donationCreatedAt;
    private LocalDateTime donationUpdatedAt;
}
