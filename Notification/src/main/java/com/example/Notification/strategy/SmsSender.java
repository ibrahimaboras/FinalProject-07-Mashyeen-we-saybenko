package com.example.Notification.strategy;

import org.springframework.stereotype.Component;

@Component("SMS")
public class SmsSender implements NotificationSender {

    @Override
    public void send(String to, String message) {
        // simulate sending
        System.out.println("SMS sent to " + to + ": " + message);
    }
}
