package com.volunteer.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationFollowResponse {
    private Long id;
    private String organizationName;
    private String description;
    private String logo;
}
