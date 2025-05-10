package com.example.Notification.listener;

import com.example.Notification.dto.BookingNotificationEvent;
import com.example.Notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingNotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "booking-notification")
    public void listen(BookingNotificationEvent event) {
        // Just delegate to the service
        notificationService.handleBookingNotification(event);
    }
}
