package com.volunteer.dto.volunteer;

import java.time.LocalDateTime;
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

    public VolunteerResponse() {}

    public VolunteerResponse(Long id, String username, String email, String pic, String contact, Long accountId, boolean isActive, LocalDateTime deletedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.pic = pic;
        this.contact = contact;
        this.accountId = accountId;
        this.isActive = isActive;
        this.deletedAt = deletedAt;
    }
} 