package com.example.Notification.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification_templates")
public class NotificationTemplate {

    @Id
    private String templateId;
    private NotificationType type;
    private String title;
    private String content;

    public NotificationTemplate() {}

    public NotificationTemplate(String templateId, NotificationType type, String title, String content) {
        this.templateId = templateId;
        this.type = type;
        this.title = title;
        this.content = content;
    }

    // Getters
    public String getTemplateId() {
        return templateId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    // Setters
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NotificationTemplate{" +
                "templateId='" + templateId + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
