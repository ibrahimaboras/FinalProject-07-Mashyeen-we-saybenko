package com.example.Notification.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BOOKING_NOTIFICATION_QUEUE = "booking-notification";

    @Bean
    public Queue bookingNotificationQueue() {
        return new Queue(BOOKING_NOTIFICATION_QUEUE, true); // durable = true
    }
}
