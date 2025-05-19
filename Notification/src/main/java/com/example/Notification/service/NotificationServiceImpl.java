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

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        NotificationTemplate template = templateRepository.findByType(event.getType());
        if (template == null) {
            throw new IllegalArgumentException("No template found for type: " + event.getType());
        }

        String message = event.getType() == NotificationType.EMAIL
                ? emailTemplateProcessor.generateMessage(template, event.getData())
                : smsTemplateProcessor.generateMessage(template, event.getData());

        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setBookingId(event.getBookingId());
        notification.setType(event.getType());
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());

        notificationRepository.save(notification);

        NotificationSender sender = senderMap.get(event.getType().name());
        if (sender != null) {
            sender.send(String.valueOf(event.getUserId()), message);
        } else {
            throw new IllegalStateException("No sender found for type: " + event.getType());
        }
    }

    @Override
    public void sendAndStoreNotification(Notification notification) {
        // Try to get a template based on type
        NotificationTemplate template = templateRepository.findByType(notification.getType());
        String finalMessage;

        if (template != null) {
            Map<String, String> dynamicData = Map.of(
                    "bookingId", String.valueOf(notification.getBookingId()),
                    "userId", String.valueOf(notification.getUserId())
            );

            finalMessage = notification.getType() == NotificationType.EMAIL
                    ? emailTemplateProcessor.generateMessage(template, dynamicData)
                    : smsTemplateProcessor.generateMessage(template, dynamicData);
        } else {
            // fallback to raw message if no template
            finalMessage = notification.getMessage();
        }

        notification.setMessage(finalMessage);
        notification.setTimestamp(LocalDateTime.now());

        notificationRepository.save(notification);

        NotificationSender sender = senderMap.get(notification.getType().name());
        if (sender != null) {
            sender.send(String.valueOf(notification.getUserId()), finalMessage);
        } else {
            throw new IllegalStateException("No sender found for type: " + notification.getType());
        }
    }


    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByType(type);
    }

    @Override
    public List<Notification> getNotificationsAfterTimestamp(LocalDateTime timestamp) {
        return notificationRepository.findByTimestampAfter(timestamp);
    }

    @Override
    public List<Notification> getNotificationsByUserAndType(Long userId, NotificationType type) {
        return notificationRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public List<Notification> getNotificationsByUserAndTimestamp(Long userId, LocalDateTime timestamp) {
        return notificationRepository.findByUserIdAndTimestampAfter(userId, timestamp);
    }

    @Override
    public List<Notification> getNotificationsByUserTypeAndTimestamp(Long userId, NotificationType type, LocalDateTime timestamp) {
        return notificationRepository.findByUserIdAndTypeAndTimestampAfter(userId, type, timestamp);
    }
    @Override
    public Notification createNotification(Notification notification) {
        notification.setTimestamp(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotification(String id, Notification updatedNotification) {
        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        existing.setMessage(updatedNotification.getMessage());
        existing.setType(updatedNotification.getType());
        existing.setUserId(updatedNotification.getUserId());
        existing.setBookingId(updatedNotification.getBookingId());
        return notificationRepository.save(existing);
    }

    @Override
    public void deleteNotification(String id) {
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("Notification not found");
        }
        notificationRepository.deleteById(id);
    }
    @Override
    public Optional<Notification> getNotificationById(String id){
        return notificationRepository.findById(id);
    }

}
