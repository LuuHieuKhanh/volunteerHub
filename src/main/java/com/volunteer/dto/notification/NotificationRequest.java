package com.volunteer.dto.notification;

import lombok.Data;

@Data
public class NotificationRequest {
    private String content;
    private Long organizationId;
} 