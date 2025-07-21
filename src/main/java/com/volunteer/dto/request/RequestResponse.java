package com.volunteer.dto.request;

import java.time.LocalDateTime;

import com.volunteer.enums.ERequestType;
import com.volunteer.enums.RequestStatus;
import lombok.Data;

@Data
public class RequestResponse {
    private Long id;
    private ERequestType requestType;
    private Long volunteerId;
    private Long organizationId;
    private RequestStatus status;
    private String denyReason;
    private String pic;
    private LocalDateTime requestDate;

    public RequestResponse() {}

    public RequestResponse(Long id, ERequestType requestType, Long volunteerId, Long organizationId, RequestStatus status, String denyReason, String pic, LocalDateTime requestDate) {
        this.id = id;
        this.requestType = requestType;
        this.volunteerId = volunteerId;
        this.organizationId = organizationId;
        this.status = status;
        this.denyReason = denyReason;
        this.pic = pic;
        this.requestDate = requestDate;
    }
} 