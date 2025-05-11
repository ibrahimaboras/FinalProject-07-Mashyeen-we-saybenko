package com.example.Notification.repository;

import com.example.Notification.model.Notification;
import com.example.Notification.model.NotificationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    /**
     * Find all notifications for a specific user.
     */
    List<Notification> findByUserId(Long userId);

    /**
     * Find all notifications of a specific type (EMAIL or SMS).
     */
    List<Notification> findByType(NotificationType type);

    /**
     * Find all notifications created after a specific timestamp.
     */
    List<Notification> findByTimestampAfter(LocalDateTime timestamp);

    /**
     * Find all notifications by user and type.
     */
    List<Notification> findByUserIdAndType(Long userId, NotificationType type);

    /**
     * Find all notifications by user and after a certain timestamp.
     */
    List<Notification> findByUserIdAndTimestampAfter(Long userId, LocalDateTime timestamp);

    /**
     * Find all notifications by user, type, and after a certain timestamp.
     */
    List<Notification> findByUserIdAndTypeAndTimestampAfter(Long userId, NotificationType type, LocalDateTime timestamp);
}
