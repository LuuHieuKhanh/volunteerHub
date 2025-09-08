package com.volunteer.dto.volunteer;

import java.time.LocalDateTime;
import java.util.Optional;

import com.volunteer.enums.Role;
import lombok.Data;

@Data
public class VolunteerResponse {
    private Long id;
    private String username;
    private String email;
    private String pic;
    private String contact;
    private Long accountId;
    private boolean isActive;
    private LocalDateTime deletedAt;
    private Role role;
    private Optional<Long> organizationId = Optional.empty();

    public VolunteerResponse() {}

    public VolunteerResponse(Long id, String username, String email, String pic, String contact, Long accountId, boolean isActive, LocalDateTime deletedAt, Role role, Optional<Long> organizationId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.pic = pic;
        this.contact = contact;
        this.accountId = accountId;
        this.isActive = isActive;
        this.deletedAt = deletedAt;
        this.role = role;
        this.organizationId = organizationId;
    }
} 