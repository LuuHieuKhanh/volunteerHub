package com.volunteer.dto.admin;

import com.volunteer.dto.request.RequestListResponse;
import lombok.Data;

import java.util.List;

@Data
public class AdminDashboardResponse {

    private Long totalVolunteers;
    private Long totalOrganizations;
    private Long totalCharityEvents;
    private Long totalDonationEvents;
    private Long totalPendingRequests;
    private List<RequestListResponse> pendingRequests;
}
