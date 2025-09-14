package com.volunteer.service;

import com.volunteer.dto.admin.AdminDashboardResponse;
import com.volunteer.dto.request.RequestListResponse;
import com.volunteer.repository.VolunteerRepository;
import com.volunteer.repository.OrganizationRepository;
import com.volunteer.repository.CharityEventRepository;
import com.volunteer.repository.DonationEventRepository;
import com.volunteer.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDashboardService {

    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardService.class);

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private CharityEventRepository charityEventRepository;

    @Autowired
    private DonationEventRepository donationEventRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    public AdminDashboardResponse getDashboardData() {
        logger.info("Getting admin dashboard data");

        AdminDashboardResponse response = new AdminDashboardResponse();

        // Get total counts
        response.setTotalVolunteers(volunteerRepository.count());
        response.setTotalOrganizations(organizationRepository.count());
        response.setTotalCharityEvents(charityEventRepository.count());
        response.setTotalDonationEvents(donationEventRepository.count());
        response.setTotalPendingRequests(requestRepository.countByStatus(com.volunteer.enums.RequestStatus.PENDING));

        // Get pending requests list
        List<RequestListResponse> pendingRequests = requestService.getPendingRequests();
        response.setPendingRequests(pendingRequests);

        return response;
    }
}
