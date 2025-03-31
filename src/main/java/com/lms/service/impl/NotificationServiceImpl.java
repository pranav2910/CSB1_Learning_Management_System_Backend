package com.lms.service.impl;

import com.lms.dto.response.NotificationResponse;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Notification;
//import com.lms.model.User;
import com.lms.repository.NotificationRepository;
import com.lms.repository.UserRepository;
import com.lms.security.SecurityUtils;
import com.lms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void sendNotification(String userId, String title, String message, Notification.NotificationType type) {
        // Keep user validation but remove the unused assignment
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        notificationRepository.save(
            Notification.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(new Date())
                .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int getUnreadNotificationCount(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        
        if (!notification.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new SecurityException("Cannot mark another user's notification as read");
        }
        
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        
        if (!notification.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new SecurityException("Cannot delete another user's notification");
        }
        
        notificationRepository.delete(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType().toString())
                .isRead(notification.getIsRead())
                .createdAt(convertToLocalDateTime(notification.getCreatedAt()))
                .build();
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date != null ? new java.sql.Timestamp(date.getTime()).toLocalDateTime() : null;
    }
}