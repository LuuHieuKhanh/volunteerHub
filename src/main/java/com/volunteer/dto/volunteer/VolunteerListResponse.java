package com.volunteer.dto.volunteer;

import com.volunteer.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VolunteerListResponse {

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
    private boolean isDeleted;

    // Organization info (if volunteer has organization)
    private String organizationName;
    private Long organizationId;
}
