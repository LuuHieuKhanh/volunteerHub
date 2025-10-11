package com.volunteer.dto.charity;

import com.volunteer.enums.EEventStatus;
import com.volunteer.enums.EJoinStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VolunteerCharityEventHistoryResponse {

    private Long id;
    private Long charityEventId;
    private String charityEventName;
    private String charityEventDescription;
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
    private LocalDateTime eventCreatedAt;
    private LocalDateTime eventUpdatedAt;
    private Long organizationId;
    private String organizationName;
    private String organizationDescription;
    private EJoinStatus joinStatus;
    private LocalDateTime participationCreatedAt;
    private LocalDateTime participationUpdatedAt;
}