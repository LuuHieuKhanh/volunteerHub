package com.volunteer.dto.request;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RequestResponse {
    private Long id;
    private String requestType;
    private Long volunteerId;
    private Long organizationId;
    private String status;
    private String denyReason;
    private String pic;
    private LocalDateTime requestDate;

    public RequestResponse() {}

    public RequestResponse(Long id, String requestType, Long volunteerId, Long organizationId, String status, String denyReason, String pic, LocalDateTime requestDate) {
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