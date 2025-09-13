package com.volunteer.dto.request;

import com.volunteer.enums.RequestStatus;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class RequestStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private RequestStatus status;

    private String denyReason;
}
