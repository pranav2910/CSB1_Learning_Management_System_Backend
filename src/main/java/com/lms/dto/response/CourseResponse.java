package com.lms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CourseResponse {
    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private String instructorName;
    private String thumbnailUrl;
    private Double averageRating;
    private Long totalStudents;
    private String categoryName;
    private LocalDateTime createdAt;
    private Boolean isPublished;
    private List<String> learningObjectives;
    private Long totalContentMinutes;
}