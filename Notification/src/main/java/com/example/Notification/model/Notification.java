package com.example.Notification.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notifications")

public class Notification {
    @Id
    private String id;                      // MongoDB unique ID (automatically generated)

    private Long userId;                    // Who receives the notification
    private Long bookingId;                 // Related booking

    private NotificationType type;          // EMAIL or SMS
    private String message;                 // Final rendered message (with template filled in)
    private LocalDateTime timestamp;        // When it was created
}
