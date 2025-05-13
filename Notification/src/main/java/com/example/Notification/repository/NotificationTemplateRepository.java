package com.example.Notification.repository;

import com.example.Notification.model.NotificationTemplate;
import com.example.Notification.model.NotificationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends MongoRepository<NotificationTemplate, String> {

    /**
     * Find a template by type (EMAIL or SMS).
     */
    NotificationTemplate findByType(NotificationType type);
}
