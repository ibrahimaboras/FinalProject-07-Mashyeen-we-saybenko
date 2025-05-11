package com.example.Notification.controller;

import com.example.Notification.model.Notification;
import com.example.Notification.model.NotificationType;
import com.example.Notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/search")
    public List<Notification> search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after
    ) {
        if (userId != null && type != null && after != null) {
            return notificationRepository.findByUserIdAndTypeAndTimestampAfter(userId, type, after);
        }
        if (userId != null && type != null) {
            return notificationRepository.findByUserIdAndType(userId, type);
        }
        if (userId != null && after != null) {
            return notificationRepository.findByUserIdAndTimestampAfter(userId, after);
        }
        if (userId != null) {
            return notificationRepository.findByUserId(userId);
        }
        if (type != null) {
            return notificationRepository.findByType(type);
        }
        if (after != null) {
            return notificationRepository.findByTimestampAfter(after);
        }
        return notificationRepository.findAll();
    }
}
