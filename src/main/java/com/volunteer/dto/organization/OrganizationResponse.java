package com.volunteer.dto.organization;

import lombok.Data;

@Data
public class OrganizationResponse {
    private Long id;
    private String organizationName;
    private String description;
    private Long volunteerId;

    public OrganizationResponse() {}

    public OrganizationResponse(Long id, String organizationName, String description, Long volunteerId) {
        this.id = id;
        this.organizationName = organizationName;
        this.description = description;
        this.volunteerId = volunteerId;
    }
} 