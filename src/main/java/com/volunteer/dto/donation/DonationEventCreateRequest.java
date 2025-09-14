package com.volunteer.dto.donation;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class DonationEventCreateRequest {

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Money need is required")
    @DecimalMin(value = "0.01", message = "Money need must be greater than 0")
    private BigDecimal moneyNeed;

    private EEventStatus eventStatus = EEventStatus.UPCOMING;

    private boolean hasDonate = false;

    private String note;

    private String qrPic;

    private String bankAccount;

    private String pic;
}
