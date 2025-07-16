package com.volunteer.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApproveRejectRequest {
    @NotNull
    private Long requestId;
    @NotNull
    private Boolean approve;
    private String denyReason;
} 