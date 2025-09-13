package com.volunteer.dto.donation;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DonationEventResponse {

    private Long id;

    private String donationName;

    private Long organizationId;

    private String description;

    private String destination;

    private LocalDateTime dateStart;

    private LocalDateTime dateEnd;

    private BigDecimal targetAmount;

    private BigDecimal actualAmount;

    private String note;

    private String pic;

    private EEventStatus eventStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
