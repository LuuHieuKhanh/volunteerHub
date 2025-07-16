package com.volunteer.dto.event;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class DonationEventRequest {
    @NotNull
    private Long organizationId;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal moneyNeed;

    private String note;
    private String qrPic;
    private String bankAccount;
    private String pic;
} 