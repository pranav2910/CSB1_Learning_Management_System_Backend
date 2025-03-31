package com.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String userId;
    private String title;
    private String message;
    private String type;  // "SYSTEM", "COURSE", "PAYMENT"
    private boolean isRead;
    private LocalDateTime createdAt;
    private String relatedCourseId;
    private String relatedPaymentId;
}