package com.example.Notification.controller;

import com.example.Notification.dto.BookingNotificationEvent;
import com.example.Notification.model.Notification;
import com.example.Notification.model.NotificationType;
import com.example.Notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 1. Create Notification
    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationService.createNotification(notification);
    }

    // 2. Update Notification
    @PutMapping("/{id}")
    public Notification updateNotification(@PathVariable String id, @RequestBody Notification updatedNotification) {
        return notificationService.updateNotification(id, updatedNotification);
    }

    // 3. Delete Notification
    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
    }

    // 4. Send Notification using Booking Event (Strategy + Template)
    @PostMapping("/send")
    public void sendNotification(@RequestBody BookingNotificationEvent event) {
        notificationService.handleBookingNotification(event);
    }

    // 5. Handle booking event (same method reused)
    @PostMapping("/event")
    public void handleBookingEvent(@RequestBody BookingNotificationEvent event) {
        notificationService.handleBookingNotification(event);
    }

    // 6. Get all notifications
    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable String id) {
        if(!notificationService.getNotificationById(id).isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(notificationService.getNotificationById(id).get());
    }
    // 7. Get notifications by user ID
    @GetMapping("/user/{userId}")
    public List<Notification> getByUserId(@PathVariable Long userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    // 8. Get notifications by type
    @GetMapping("/type/{type}")
    public List<Notification> getByType(@PathVariable NotificationType type) {
        return notificationService.getNotificationsByType(type);
    }

    // 9. Get notifications after a given date
    @GetMapping("/after")
    public List<Notification> getByDateAfter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        return notificationService.getNotificationsAfterTimestamp(timestamp);
    }

    // 10. Combined filters: user and type
    @GetMapping("/user/{userId}/type/{type}")
    public List<Notification> getByUserAndType(
            @PathVariable Long userId,
            @PathVariable NotificationType type) {
        return notificationService.getNotificationsByUserAndType(userId, type);
    }

    // 11. Combined filters: user and date
    @GetMapping("/user/{userId}/after")
    public List<Notification> getByUserAndDateAfter(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        return notificationService.getNotificationsByUserAndTimestamp(userId, timestamp);
    }

    // 12. Combined filters: user, type, and date
    @GetMapping("/user/{userId}/type/{type}/after")
    public List<Notification> getByUserTypeAndDateAfter(
            @PathVariable Long userId,
            @PathVariable NotificationType type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        return notificationService.getNotificationsByUserTypeAndTimestamp(userId, type, timestamp);
    }
}
