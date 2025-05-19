package com.example.Notification.listener;

import lombok.Data;

import java.util.UUID;

@Data
public class CancelBookingEvent {
    private UUID bookingId;
    private String userId;

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
