package com.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePerformanceResponse {
    private String courseId;
    private String courseTitle;
    private Long enrollments;
    private BigDecimal revenue;
    private Double averageRating;
    private Integer completionRate;
}