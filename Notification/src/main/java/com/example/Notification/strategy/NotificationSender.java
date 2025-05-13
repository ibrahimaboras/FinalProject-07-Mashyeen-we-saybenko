package com.example.Notification.strategy;

public interface NotificationSender {
    void send(String to, String message);
}