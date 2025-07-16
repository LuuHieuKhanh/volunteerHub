package com.volunteer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrganizationUpgradeRequest {
    @NotNull
    private Long volunteerId;
    @NotBlank
    private String organizationName;
    private String description;
} 