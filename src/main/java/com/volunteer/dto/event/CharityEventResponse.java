package com.volunteer.dto.event;

import com.volunteer.enums.EEventStatus;
import com.volunteer.enums.EJoinStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CharityEventResponse {

    private Long id;
    private String charityName;
    private Long organizationId;
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
    private EJoinStatus joinStatus;

    public CharityEventResponse() {
    }

    public CharityEventResponse(Long id, String charityName, Long organizationId, String description, String todo, String requirement, String destination, LocalDateTime dateStart, LocalDateTime dateEnd, Long numVolunteerRequire, Long numVolunteerActual, String note, String pic, EEventStatus eventStatus) {
        this.id = id;
        this.charityName = charityName;
        this.organizationId = organizationId;
        this.description = description;
        this.todo = todo;
        this.requirement = requirement;
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
