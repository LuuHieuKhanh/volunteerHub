package com.volunteer.dto.charity;

import com.volunteer.enums.EJoinStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipantResponse {

    private Long id;
    private Long volunteerId;
    private String volunteerFullName;
    private String volunteerContact;
    private String volunteerEmail;
    private EJoinStatus joinStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}