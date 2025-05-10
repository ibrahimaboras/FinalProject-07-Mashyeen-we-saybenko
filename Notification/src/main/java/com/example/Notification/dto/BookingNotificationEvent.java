package com.example.Notification.dto;

import com.example.Notification.model.NotificationType;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingNotificationEvent {
    // DTO fields from RabbitMQ
    private Long userId;
    private Long bookingId;
    private NotificationType type;
    private Map<String, String> data; // dynamic values like {name: John, bookingId: 456}

    public Long getUserId() {
        return userId;
    }


    public NotificationType getType() {
        return type;
    }

    public Long getBookingId() {
        return bookingId;
    }


    public Map<String, String> getData() {
        return data;
    }


}
