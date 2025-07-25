package com.volunteer.dto.request;

import com.volunteer.enums.RequestStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRegisterResponse {

    private Long id;

    private String organizationName;

    private String founderFullName;

    private String email;

    private String phoneNumber;

    private String logoUrl;

    private String certificateUrl;

    private RequestStatus status; // e.g. PENDING / APPROVED / REJECTED

    private String message;
}