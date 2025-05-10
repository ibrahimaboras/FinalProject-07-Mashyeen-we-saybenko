package com.example.Notification.service;
import com.example.Notification.dto.BookingNotificationEvent;

public interface NotificationService {
    // define service contract
    void handleBookingNotification(BookingNotificationEvent event);
}
