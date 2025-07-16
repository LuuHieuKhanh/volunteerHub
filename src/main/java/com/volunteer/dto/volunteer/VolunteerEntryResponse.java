package com.volunteer.dto.volunteer;

import lombok.Data;

@Data
public class VolunteerEntryResponse {
    private Long id;
    private String username;
    private String email;
    private String pic;
    private String contact;

    public VolunteerEntryResponse() {}

    public VolunteerEntryResponse(Long id, String username, String email, String pic, String contact) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.pic = pic;
        this.contact = contact;
    }
} 