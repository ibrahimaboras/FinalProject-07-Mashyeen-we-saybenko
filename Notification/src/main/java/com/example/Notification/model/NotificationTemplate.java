package com.example.Notification.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notification_templates")

public class NotificationTemplate {
    @Id
    private String templateId;        // Unique template ID

    private NotificationType type;    // EMAIL or SMS
    private String title;             // E.g., "Booking Confirmed"
    private String content;           // E.g., "Dear {name}, your booking #{bookingId} is confirmed!"

}
