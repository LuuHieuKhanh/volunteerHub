package com.volunteer.dto.donation;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class DonationEventStatusUpdateRequest {

    @NotNull(message = "Event status is required")
    private EEventStatus eventStatus;
}
