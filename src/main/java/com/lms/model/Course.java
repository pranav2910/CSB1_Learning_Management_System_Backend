package com.lms.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "courses")
public class Course {
    @Id
    private String id;

    @Indexed
    private String title;
    
    private String description;
    private BigDecimal price;
    
    @Indexed
    private String instructorId;
    
    @Indexed
    private String categoryId;
    
    private String thumbnailUrl;
    private String previewVideoUrl;
    
    @Builder.Default
    private List<String> learningObjectives = new ArrayList<>();
    
    @Builder.Default
    private Double averageRating = 0.0;
    
    @Builder.Default
    private Integer totalStudents = 0;
    
    @Builder.Default
    private Integer totalReviews = 0;
    
    @Indexed
    @Builder.Default
    private Boolean isPublished = false;
    
    @Indexed
    @Builder.Default
    private Boolean isApproved = false;
    
    private Date createdAt;
    private Date updatedAt;
    
    @Builder.Default
    private List<String> contentIds = new ArrayList<>();
    
    @DBRef(lazy = true)
    @Builder.Default
    private List<Content> contents = new ArrayList<>();

    // Default constructor for framework use
    public Course() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // All-args constructor for builder
    public Course(String id, String title, String description, BigDecimal price, 
                String instructorId, String categoryId, String thumbnailUrl, 
                String previewVideoUrl, List<String> learningObjectives, 
                Double averageRating, Integer totalStudents, Integer totalReviews, 
                Boolean isPublished, Boolean isApproved, Date createdAt, 
                Date updatedAt, List<String> contentIds, List<Content> contents) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.instructorId = instructorId;
        this.categoryId = categoryId;
        this.thumbnailUrl = thumbnailUrl;
        this.previewVideoUrl = previewVideoUrl;
        this.learningObjectives = learningObjectives != null ? learningObjectives : new ArrayList<>();
        this.averageRating = averageRating != null ? averageRating : 0.0;
        this.totalStudents = totalStudents != null ? totalStudents : 0;
        this.totalReviews = totalReviews != null ? totalReviews : 0;
        this.isPublished = isPublished != null ? isPublished : false;
        this.isApproved = isApproved != null ? isApproved : false;
        this.createdAt = createdAt != null ? createdAt : new Date();
        this.updatedAt = updatedAt != null ? updatedAt : new Date();
        this.contentIds = contentIds != null ? contentIds : new ArrayList<>();
        this.contents = contents != null ? contents : new ArrayList<>();
    }

    // Helper method to add content
    public void addContent(Content content) {
        if (content != null) {
            if (this.contents == null) {
                this.contents = new ArrayList<>();
            }
            this.contents.add(content);
            
            if (this.contentIds == null) {
                this.contentIds = new ArrayList<>();
            }
            if (content.getId() != null && !this.contentIds.contains(content.getId())) {
                this.contentIds.add(content.getId());
            }
            this.updatedAt = new Date();
        }
    }

    // Helper method to remove content
    public void removeContent(String contentId) {
        if (contentId != null) {
            if (this.contents != null) {
                this.contents.removeIf(c -> contentId.equals(c.getId()));
            }
            if (this.contentIds != null) {
                this.contentIds.remove(contentId);
            }
            this.updatedAt = new Date();
        }
    }
}