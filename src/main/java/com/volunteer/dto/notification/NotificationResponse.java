package com.volunteer.dto.notification;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private String content;
    private Long eventId;
    private LocalDateTime createdAt;
    private boolean read;
} 