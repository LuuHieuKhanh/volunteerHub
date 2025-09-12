package com.volunteer.dto.volunteer;

import com.volunteer.dto.donation.DonationEntryResponse;
import com.volunteer.dto.event.CharityEventResponse;
import com.volunteer.dto.organization.OrganizationResponse;
import com.volunteer.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VolunteerDetailResponse {

    private Long id;
    private String fullName;
    private String email;
    private String contact;
    private String pic;
    private boolean isActive;
    private boolean isBanned;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean isDeleted;

    // Organization info (if volunteer has organization)
    private OrganizationResponse organization;

    // List of charity events participated
    private List<CharityEventResponse> participatedEvents;

    // List of donation history
    private List<DonationEntryResponse> donationHistory;

    // Statistics
    private Long totalEventsParticipated;
    private Long totalDonationAmount;
    private Long totalDonationCount;
}
