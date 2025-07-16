package com.volunteer.dto.donation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class DonationRequest {
    @NotNull
    private Long donationEventId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal donateAmount;

    private String note;
} 