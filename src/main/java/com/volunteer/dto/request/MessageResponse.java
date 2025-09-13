package com.volunteer.dto.request;

import lombok.Data;

@Data
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
