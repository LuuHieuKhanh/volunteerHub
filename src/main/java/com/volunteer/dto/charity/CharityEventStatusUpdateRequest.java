package com.volunteer.dto.charity;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class CharityEventStatusUpdateRequest {

    @NotNull(message = "Event status is required")
    private EEventStatus eventStatus;
}