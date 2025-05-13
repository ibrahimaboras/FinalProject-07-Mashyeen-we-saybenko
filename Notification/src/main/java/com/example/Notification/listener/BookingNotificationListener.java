package com.example.Notification.listener;

import com.example.Notification.dto.BookingNotificationEvent;
import com.example.Notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookingNotificationListener {

    private final NotificationService notificationService;

    public BookingNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "booking-notification")
    public void listen(BookingNotificationEvent event) {
        // Delegate to service
        notificationService.handleBookingNotification(event);
    }
}
