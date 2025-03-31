package com.lms.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EnrollmentResponse {
    private String id;
    private String courseId;
    private String courseTitle;
    private LocalDateTime enrolledAt;
    private Double progressPercentage;
    private List<String> completedContentIds;
    private LocalDateTime completedAt;
}