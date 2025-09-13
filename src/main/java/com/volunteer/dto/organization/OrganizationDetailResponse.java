package com.volunteer.dto.organization;

import com.volunteer.dto.event.CharityEventResponse;
import com.volunteer.dto.donation.DonationEventResponse;
import com.volunteer.enums.EAccountStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrganizationDetailResponse {

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

    // Statistics
    private Long totalVolunteersParticipated;

    private BigDecimal totalDonationAmount;

    private Long totalCharityEvents;

    private Long totalDonationEvents;

    // Event lists
    private List<CharityEventResponse> charityEvents;

    private List<DonationEventResponse> donationEvents;

    // Active volunteers
    private List<VolunteerActiveResponse> activeVolunteers;
}
