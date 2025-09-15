package com.volunteer.dto.charity;

import com.volunteer.enums.EEventStatus;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
public class CharityEventCreateRequest {

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotBlank(message = "Charity name is required")
    private String charityName;

    private String description;

    private String todo;

    private String requirement;

    private String destination;

    @NotNull(message = "Date start is required")
    private LocalDateTime dateStart;

    @NotNull(message = "Date end is required")
    private LocalDateTime dateEnd;

    @NotNull(message = "Number of volunteers required is required")
    @Min(value = 1, message = "Number of volunteers required must be at least 1")
    private Long numVolunteerRequire;

    private String note;

    private String pic;

    private EEventStatus eventStatus = EEventStatus.UPCOMING;
}
