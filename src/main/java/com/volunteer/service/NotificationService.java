package com.volunteer.service;

import com.volunteer.dto.notification.NotificationRequest;
import com.volunteer.dto.notification.NotificationResponse;
import com.volunteer.entity.Notification;
import com.volunteer.repository.NotificationRepository;
import com.volunteer.repository.VolunteerCharityEventRepository;
import com.volunteer.enums.EJoinStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private VolunteerCharityEventRepository volunteerCharityEventRepository;

    @Transactional
    public void notifyEventParticipants(Long eventId, String content, Long organizationId) {
        // Fetch all volunteers who participated in the event (REGISTERED or COMPLETED)
        List<EJoinStatus> statuses = List.of(EJoinStatus.REGISTERED, EJoinStatus.COMPLETED);
        for (EJoinStatus status : statuses) {
            List<com.volunteer.entity.VolunteerCharityEvent> participations =
                volunteerCharityEventRepository.findByCharityEvent_IdAndJoinStatus(eventId, status);
            for (com.volunteer.entity.VolunteerCharityEvent participation : participations) {
                Notification notification = Notification.builder()
                    .content(content)
                    .eventId(eventId)
                    .volunteerId(participation.getVolunteer().getId())
                    .organizationId(organizationId)
                    .createdAt(LocalDateTime.now())
                    .read(false)
                    .build();
                notificationRepository.save(notification);
            }
        }
    }

    public List<NotificationResponse> getUserNotifications(Long volunteerId) {
        return notificationRepository.findByVolunteerId(volunteerId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setContent(notification.getContent());
        response.setEventId(notification.getEventId());
        response.setCreatedAt(notification.getCreatedAt());
        response.setRead(notification.isRead());
        return response;
    }
} 