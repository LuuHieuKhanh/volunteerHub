package com.volunteer.dto.organization;

import lombok.Data;

@Data
public class OrganizationUpdateRequest {

    private String organizationName;

    private String description;

    private String certificate;

    private String logo;
}
