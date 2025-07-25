package com.volunteer.dto.request;

import java.time.LocalDateTime;

import com.volunteer.Utility.FileUtil;
import com.volunteer.enums.ERequestType;
import com.volunteer.enums.RequestStatus;
import lombok.Data;

@Data
public class RequestResponse {
    private Long id;
    private ERequestType requestType;
    private Long volunteerId;
    private RequestStatus status;
    private String denyReason;
    private String organizationName;
    private String description;
    private String logo;
    private String certificate;
    private String email;
    private String phone;
    private String fullName;
    private LocalDateTime updatedAt;
    public RequestResponse() {}

    public RequestResponse(Long id, String organizationName, String fullName, String email, String phone, String description, String denyReason, String logo, String certificate, ERequestType requestType, Long volunteerId, RequestStatus status, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationName = organizationName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.description = description;
        this.denyReason = denyReason;
        this.logo = FileUtil.buildFileUrl(logo);
        this.certificate = FileUtil.buildFileUrl(certificate);
        this.requestType = requestType;
        this.volunteerId = volunteerId;
        this.status = status;
        this.updatedAt = updatedAt;
    }
} 