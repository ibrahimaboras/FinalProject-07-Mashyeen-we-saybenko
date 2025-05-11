package com.example.Notification.service;

import com.example.Notification.dto.BookingNotificationEvent;
import com.example.Notification.model.Notification;
import com.example.Notification.model.NotificationTemplate;
import com.example.Notification.model.NotificationType;
import com.example.Notification.repository.NotificationRepository;
import com.example.Notification.repository.NotificationTemplateRepository;
import com.example.Notification.strategy.NotificationSender;
import com.example.Notification.template.EmailTemplateProcessor;
import com.example.Notification.template.SmsTemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final Map<String, NotificationSender> senderMap;
    private final EmailTemplateProcessor emailTemplateProcessor;
    private final SmsTemplateProcessor smsTemplateProcessor;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            NotificationTemplateRepository templateRepository,
            Map<String, NotificationSender> senderMap,
            EmailTemplateProcessor emailTemplateProcessor,
            SmsTemplateProcessor smsTemplateProcessor
    ) {
        this.notificationRepository = notificationRepository;
        this.templateRepository = templateRepository;
        this.senderMap = senderMap;
        this.emailTemplateProcessor = emailTemplateProcessor;
        this.smsTemplateProcessor = smsTemplateProcessor;
    }

    @Override
    public void handleBookingNotification(BookingNotificationEvent event) {
        // 1. Get the correct template
        NotificationTemplate template = templateRepository.findByType(event.getType());
        if (template == null) {
            throw new IllegalArgumentException("No template found for type: " + event.getType());
        }

        // 2. Render message content
        String message = event.getType() == NotificationType.EMAIL
                ? emailTemplateProcessor.generateMessage(template, event.getData())
                : smsTemplateProcessor.generateMessage(template, event.getData());

        // 3. Save notification to MongoDB
        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setBookingId(event.getBookingId());
        notification.setType(event.getType());
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());

        notificationRepository.save(notification);

        // 4. Send notification using appropriate strategy
        NotificationSender sender = senderMap.get(event.getType().name());
        if (sender != null) {
            sender.send(String.valueOf(event.getUserId()), message);
        } else {
            throw new IllegalStateException("No sender found for type: " + event.getType());
        }
    }
}
