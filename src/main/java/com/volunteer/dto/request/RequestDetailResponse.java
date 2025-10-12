package com.volunteer.dto.request;

import com.volunteer.enums.ERequestType;
import com.volunteer.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDetailResponse {

    private Long id;
    private ERequestType requestType;
    private RequestStatus status;
    private String denyReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Organization info
    private Long organizationId;
    private String organizationName;
    private String organizationDescription;
    private String organizationContact;
    private String organizationAddress;
    private String organizationEmail;
    private String organizationWebsite;
    private String organizationLogo;
    private String organizationCertificate;
    private LocalDateTime organizationCreatedAt;
    private LocalDateTime organizationUpdatedAt;

    // Volunteer info
    private Long volunteerId;
    private String volunteerFullName;
    private String volunteerContact;
    private String volunteerEmail;
    private String volunteerAddress;
    private String volunteerAvatar;
    private LocalDateTime volunteerCreatedAt;
    private LocalDateTime volunteerUpdatedAt;
}
