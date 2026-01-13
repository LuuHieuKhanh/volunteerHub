package com.volunteer.dto.event;

import java.time.LocalDateTime;

import com.volunteer.enums.EJoinStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class VolunteerEventParticipationResponse {
    private Long eventId;
    private Long id;
    private String fullName;
    private EJoinStatus joinStatus;
    private LocalDateTime joinDate;
    private String contact;
    private String email;
    private boolean checkin;
    public VolunteerEventParticipationResponse() {}

}