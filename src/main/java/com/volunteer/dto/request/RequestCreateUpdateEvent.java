package com.volunteer.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestCreateUpdateEvent {
    @NotNull
    private Long eventId;
    @NotNull
    private String eventType; // CHARITY or DONATION
    @NotNull
    private String requestType; // CREATE, EDIT, DELETE
    private String note;
} 