package com.volunteer.dto.organization;

import com.volunteer.enums.EAccountStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrganizationListResponse {

    private Long id;

    private String organizationName;

    private String description;

    private String certificate;

    private String logo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isDeleted;

    // Owner information
    private String ownerName;

    private String ownerEmail;

    private String ownerContact;

    private EAccountStatus ownerStatus;

    private boolean ownerIsActive;

    private boolean ownerIsBanned;
}
