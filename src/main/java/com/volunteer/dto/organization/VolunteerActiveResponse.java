package com.volunteer.dto.organization;

import lombok.Data;

@Data
public class VolunteerActiveResponse {

    private Long id;
    private String fullName;
    private String contact;
    private String email;
    private boolean isActive;
    private boolean isBanned;
}
