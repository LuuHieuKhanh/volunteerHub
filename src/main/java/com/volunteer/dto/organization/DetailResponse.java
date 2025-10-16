package com.volunteer.dto.organization;

import com.volunteer.dto.event.CharityEventResponseList;
import com.volunteer.dto.donation.DonationEventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailResponse {
    private Long id;
    private String organizationName;
    private String description;
    private String logo;
    private String certificate;
    private String contact;
    private String address;
    private String owner;
    private boolean followed; // volunteer đã follow org chưa
    private String reason;
    private List<CharityEventResponseList> charities;
    private List<DonationEventResponse> donations; // 👈 thêm ở đây

}
