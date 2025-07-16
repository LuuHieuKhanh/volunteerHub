package com.volunteer.dto.event;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VolunteerEventParticipationResponse {
    private Long eventId;
    private Long volunteerId;
    private String joinStatus;
    private LocalDateTime joinDate;

    public VolunteerEventParticipationResponse() {}

    public VolunteerEventParticipationResponse(Long eventId, Long volunteerId, String joinStatus, LocalDateTime joinDate) {
        this.eventId = eventId;
        this.volunteerId = volunteerId;
        this.joinStatus = joinStatus;
        this.joinDate = joinDate;
    }
} 