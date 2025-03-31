package com.lms.model;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Builder
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String courseId;
    private String userId;
    private Integer rating;
    private String comment;
    @Builder.Default
    private Date createdAt = new Date();
    private Date updatedAt;
}