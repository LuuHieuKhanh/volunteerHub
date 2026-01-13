package com.volunteer.dto.donation;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class DonationEventUpdateRequest {

    private Long organizationId;
    private String title;
    private String description;

    @DecimalMin(value = "0.01", message = "Money need must be greater than 0")
    private BigDecimal moneyNeed;

    private EEventStatus eventStatus;
    private boolean hasDonate;
    private String note;
    private String qrPic;
    private String bankAccount;
    private String pic;
}