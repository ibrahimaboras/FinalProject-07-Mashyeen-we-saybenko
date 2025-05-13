package com.example.Notification.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private Long userId;
    private Long bookingId;
    private NotificationType type;
    private String message;
    private LocalDateTime timestamp;

    public Notification() {}

    public Notification(String id, Long userId, Long bookingId, NotificationType type, String message, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.bookingId = bookingId;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", bookingId=" + bookingId +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
