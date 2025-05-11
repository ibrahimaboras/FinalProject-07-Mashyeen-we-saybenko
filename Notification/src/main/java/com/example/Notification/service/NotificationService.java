package com.example.Notification.service;

import com.example.Notification.dto.BookingNotificationEvent;

public interface NotificationService {
    /**
     * Handles a notification event received from the booking system.
     * @param event BookingNotificationEvent containing data to generate and send the notification.
     */
    void handleBookingNotification(BookingNotificationEvent event);
}
