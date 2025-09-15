package com.volunteer.dto.charity;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CharityEventListResponse {

    private Long id;
    private String charityName;
    private String description;
    private String todo;
    private String requirement;
    private String destination;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private Long numVolunteerRequire;
    private Long numVolunteerActual;
    private String note;
    private String pic;
    private EEventStatus eventStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Organization info
    private Long organizationId;
    private String organizationName;
    private String organizationDescription;

    // Total participants count
    private Long totalParticipants;
}
