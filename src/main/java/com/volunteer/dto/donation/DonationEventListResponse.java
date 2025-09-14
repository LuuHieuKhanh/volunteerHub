package com.volunteer.dto.donation;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DonationEventListResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal moneyNeed;
    private EEventStatus eventStatus;
    private boolean hasDonate;
    private String note;
    private String qrPic;
    private String bankAccount;
    private String pic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Organization info
    private Long organizationId;
    private String organizationName;
    private String organizationDescription;

    // Total donated amount
    private BigDecimal totalDonatedAmount;
}
