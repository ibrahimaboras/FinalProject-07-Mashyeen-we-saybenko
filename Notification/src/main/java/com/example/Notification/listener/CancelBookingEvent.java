package com.example.Notification.listener;

import lombok.Data;

@Data
public class CancelBookingEvent {
    private String bookingId;
    private String userId;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
