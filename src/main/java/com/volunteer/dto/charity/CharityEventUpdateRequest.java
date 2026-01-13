package com.volunteer.dto.charity;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
public class CharityEventUpdateRequest {

    private Long organizationId;
    private String charityName;
    private String description;
    private String todo;
    private String requirement;
    private String destination;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;

    @Min(value = 1, message = "Number of volunteers required must be at least 1")
    private Long numVolunteerRequire;

    private String note;
    private String pic;
    private EEventStatus eventStatus;
}