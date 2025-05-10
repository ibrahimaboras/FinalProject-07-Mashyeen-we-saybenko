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
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;

    // Inject implementations via Spring
    private final Map<String, NotificationSender> senderMap;
    private final EmailTemplateProcessor emailTemplateProcessor;
    private final SmsTemplateProcessor smsTemplateProcessor;

    @Override
    //@RabbitListener(queues = "booking-notification")
    public void handleBookingNotification(BookingNotificationEvent event) {
        // 1. Get template
        NotificationTemplate template = templateRepository.findByType(event.getType());

        // 2. Format message using TemplateProcessor
        String message = event.getType() == NotificationType.EMAIL ?
                emailTemplateProcessor.generateMessage(template, event.getData()) :
                smsTemplateProcessor.generateMessage(template, event.getData());

        // 3. Save to DB
        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .bookingId(event.getBookingId())
                .type(event.getType())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // 4. Send via strategy
        NotificationSender sender = senderMap.get(event.getType().name());
        sender.send(String.valueOf(event.getUserId()), message);
    }
}
