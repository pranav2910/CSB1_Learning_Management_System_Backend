package com.lms.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "analytics")
public class Analytics {
    @Id
    private String id;
    private Date date = new Date();
    private Integer totalUsers;
    private Integer totalCourses;
    private Integer totalEnrollments;
    private BigDecimal totalRevenue;
    private Map<String, Integer> popularCategories;
    private Map<String, Integer> activeUsers;
}