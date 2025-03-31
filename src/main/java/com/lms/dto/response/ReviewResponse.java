package com.lms.dto.response;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class ReviewResponse {
    private String id;
    private String courseId;
    private String userId;
    private String userName;
    private Integer rating;
    private String comment;
    private Date createdAt;
    private Date updatedAt;
}