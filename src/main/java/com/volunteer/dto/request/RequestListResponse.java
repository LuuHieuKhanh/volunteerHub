package com.volunteer.dto.request;

import com.volunteer.enums.ERequestType;
import com.volunteer.enums.RequestStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RequestListResponse {

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

    // Volunteer info
    private Long volunteerId;
    private String volunteerFullName;
    private String volunteerContact;
    private String volunteerEmail;

    // Charity event info
    private Long charityEventId;
    private String charityEventName;
    private String destination;
    private Long numberOfVolunteers;
    private LocalDateTime charityEventDateStart;
    private LocalDateTime charityEventDateEnd;

    // Donation event info
    private Long donationEventId;
    private String donationEventName;
    private BigDecimal moneyNeed;
    private LocalDateTime donationEventDateStart;
    private LocalDateTime donationEventDateEnd;
}
