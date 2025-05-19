package com.example.Notification.dto;

import com.example.Notification.model.NotificationType;

import java.util.Map;
import java.util.UUID;

public class BookingNotificationEvent {

    private Long userId;
    private UUID bookingId;
    private NotificationType type;
    private Map<String, String> data;

    public BookingNotificationEvent() {
        // No-arg constructor
    }

    public BookingNotificationEvent(Long userId, UUID bookingId, NotificationType type, Map<String, String> data) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.type = type;
        this.data = data;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
