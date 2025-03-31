package com.lms.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank(message = "Course ID is required")
    private String courseId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;
}