package com.example.Notification.service;

import com.example.Notification.dto.BookingNotificationEvent;
import com.example.Notification.model.Notification;
import com.example.Notification.model.NotificationType;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    void handleBookingNotification(BookingNotificationEvent event);

    List<Notification> getAllNotifications();
    List<Notification> getNotificationsByUserId(Long userId);
    List<Notification> getNotificationsByType(NotificationType type);
    List<Notification> getNotificationsAfterTimestamp(LocalDateTime timestamp);
    List<Notification> getNotificationsByUserAndType(Long userId, NotificationType type);
    List<Notification> getNotificationsByUserAndTimestamp(Long userId, LocalDateTime timestamp);
    List<Notification> getNotificationsByUserTypeAndTimestamp(Long userId, NotificationType type, LocalDateTime timestamp);

    Optional<Notification> getNotificationById(String id);
    Notification createNotification(Notification notification);
    Notification updateNotification(String id, Notification updatedNotification);
    void deleteNotification(String id);


}
