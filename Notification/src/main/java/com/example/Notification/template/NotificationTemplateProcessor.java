package com.example.Notification.template;

import com.example.Notification.model.NotificationTemplate;

import java.util.Map;

public abstract class NotificationTemplateProcessor {
    protected String processTemplate(String template, Map<String, String> params) {
        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    public abstract String generateMessage(NotificationTemplate template, Map<String, String> data);
}
