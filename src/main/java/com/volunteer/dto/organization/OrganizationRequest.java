package com.volunteer.dto.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrganizationRequest {
    @NotBlank
    private String organizationName;

    private String description = "";

    private String logo = "";

    private String certificate = "";

    @NotNull
    private Long volunteerId;
} 