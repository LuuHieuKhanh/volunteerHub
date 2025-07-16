package com.volunteer.dto.event;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CharityEventResponse {
    private Long id;
    private String charityName;
    private Long organizationId;
    private String description;
    private String destination;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private Long numVolunteerRequire;
    private Long numVolunteerActual;
    private String note;
    private String pic;
    private String eventStatus;

    public CharityEventResponse() {}

    public CharityEventResponse(Long id, String charityName, Long organizationId, String description, String destination, LocalDateTime dateStart, LocalDateTime dateEnd, Long numVolunteerRequire, Long numVolunteerActual, String note, String pic, String eventStatus) {
        this.id = id;
        this.charityName = charityName;
        this.organizationId = organizationId;
        this.description = description;
        this.destination = destination;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.numVolunteerRequire = numVolunteerRequire;
        this.numVolunteerActual = numVolunteerActual;
        this.note = note;
        this.pic = pic;
        this.eventStatus = eventStatus;
    }
} 