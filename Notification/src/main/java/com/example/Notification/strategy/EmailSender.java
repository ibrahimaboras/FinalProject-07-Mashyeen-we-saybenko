package com.example.Notification.strategy;

import org.springframework.stereotype.Component;

@Component("EMAIL")
public class EmailSender implements NotificationSender {

    @Override
    public void send(String to, String message) {
        // simulate sending
        System.out.println("EMAIL sent to " + to + ": " + message);
    }
}
