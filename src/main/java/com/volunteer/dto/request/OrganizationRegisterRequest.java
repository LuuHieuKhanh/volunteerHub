package com.volunteer.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRegisterRequest {

    private Long volunteerId;

    private String organizationName = "";

    private String phoneNumber = "";

    private String reason = "";

    private String description = "";

    private MultipartFile organizationLogo; // optional

    private MultipartFile certificate; // optional
}

