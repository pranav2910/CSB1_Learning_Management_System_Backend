package com.lms.service;

import com.lms.dto.response.NotificationResponse;
import com.lms.model.Notification;
import java.util.List;

public interface NotificationService {
    void sendNotification(String userId, String title, String message, Notification.NotificationType type);
    List<NotificationResponse> getUserNotifications(String userId);
    int getUnreadNotificationCount(String userId);
    void markAsRead(String notificationId);
    void deleteNotification(String notificationId);
}