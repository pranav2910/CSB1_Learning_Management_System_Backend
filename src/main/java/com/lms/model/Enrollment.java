package com.lms.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "enrollments")
public class Enrollment {
    @Id
    private String id;
    private String studentId;
    private String courseId;
    
    @Builder.Default
    private LocalDateTime enrolledAt = LocalDateTime.now();
    
    private LocalDateTime completedAt;
    
    @Builder.Default
    private List<String> completedContentIds = new ArrayList<>();
    
    private Double progress;
    
    @Builder.Default
    private Boolean isActive = true;

    // No-args constructor
    public Enrollment() {
    }

    // All-args constructor
    public Enrollment(String id, String studentId, String courseId, 
                    LocalDateTime enrolledAt, LocalDateTime completedAt,
                    List<String> completedContentIds, Double progress, 
                    Boolean isActive) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledAt = enrolledAt != null ? enrolledAt : LocalDateTime.now();
        this.completedAt = completedAt;
        this.completedContentIds = completedContentIds != null ? completedContentIds : new ArrayList<>();
        this.progress = progress;
        this.isActive = isActive != null ? isActive : true;
    }
}