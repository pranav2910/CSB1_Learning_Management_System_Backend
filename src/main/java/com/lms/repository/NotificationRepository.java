package com.lms.repository;

import com.lms.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    
    @Query("{ 'userId': ?0, 'isRead': false }")
    int countByUserIdAndIsReadFalse(String userId);
    
    @Query("{ 'userId': ?0, 'type': ?1 }")
    List<Notification> findByUserIdAndType(String userId, Notification.NotificationType type);
}