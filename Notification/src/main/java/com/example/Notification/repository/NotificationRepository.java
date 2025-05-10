package com.example.Notification.repository;

import com.example.Notification.model.Notification;
import com.example.Notification.model.NotificationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    //MongoRepo

    List<Notification> findByUserId(Long userId); //All notifications for a specific user

    List<Notification> findByType(NotificationType type); //All EMAIL or SMS notifications

    List<Notification> findByTimestampAfter(LocalDateTime timestamp); //All notifications after a date

    List<Notification> findByUserIdAndType(Long userId, NotificationType type); //Combine filters

    List<Notification> findByUserIdAndTimestampAfter(Long userId, LocalDateTime timestamp); //User's notifications after a certain time

    List<Notification> findByUserIdAndTypeAndTimestampAfter(Long userId, NotificationType type, LocalDateTime timestamp); //Full search by user + type + date

}
