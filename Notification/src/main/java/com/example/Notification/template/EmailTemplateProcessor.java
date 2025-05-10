package com.example.Notification.template;

import com.example.Notification.model.NotificationTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailTemplateProcessor extends NotificationTemplateProcessor {

    @Override
    public String generateMessage(NotificationTemplate template, Map<String, String> data) {
        return processTemplate(template.getContent(), data);
    }
}
