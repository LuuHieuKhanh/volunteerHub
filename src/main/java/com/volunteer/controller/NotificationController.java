package com.volunteer.controller;

import com.volunteer.dto.notification.NotificationRequest;
import com.volunteer.dto.notification.NotificationResponse;
import com.volunteer.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    // Organization sends notification to event participants
    @PostMapping("/event/{eventId}")
    public ResponseEntity<?> notifyEventParticipants(@PathVariable Long eventId, @RequestBody NotificationRequest request) {
        notificationService.notifyEventParticipants(eventId, request.getContent(), request.getOrganizationId());
        return ResponseEntity.ok("Notifications sent");
    }

    // User fetches their notifications
    @GetMapping("")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@RequestParam Long volunteerId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(volunteerId));
    }

    // User marks notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }
} 