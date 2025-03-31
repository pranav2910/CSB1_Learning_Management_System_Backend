package com.lms.controller;

import com.lms.dto.response.NotificationResponse;
import com.lms.security.SecurityUtils;
import com.lms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getUserNotifications() {
        String userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadNotificationCount() {
        String userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount(userId));
    }

    @PostMapping("/{notificationId}/mark-read")
    public ResponseEntity<String> markNotificationAsRead(
            @PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}