package com.volunteer.dto.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CharityEventRequest {
    @NotBlank
    private String charityName;

    @NotNull
    private Long organizationId;

    private String description;
    private String destination;

    @NotNull
    @Future
    private LocalDateTime dateStart;

    @NotNull
    @Future
    private LocalDateTime dateEnd;

    @NotNull
    private Long numVolunteerRequire;

    private String note;
    private String pic;
} 