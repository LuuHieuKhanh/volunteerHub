package com.volunteer.dto.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrganizationCreateRequest {

    @NotBlank(message = "Organization name is required")
    private String organizationName;

    @NotBlank(message = "Description is required")
    private String description;

    private String certificate;

    private String logo;

    @NotNull(message = "Volunteer ID is required")
    private Long volunteerId;
}
