package com.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class ContentRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;

    @NotBlank(message = "Content type is required")
    @Pattern(regexp = "^(VIDEO|QUIZ|DOCUMENT)$", 
             message = "Content type must be VIDEO, QUIZ or DOCUMENT")
    private String contentType;

    @NotBlank(message = "Content URL is required")
    private String contentUrl;

    @Min(value = 0, message = "Duration must be positive")
    private Integer duration;

    @Min(value = 0, message = "Order must be positive")
    private Integer order;

    private Boolean isFreePreview = false;
    private List<QuestionRequest> questions;

    @Data
    public static class QuestionRequest {
        @NotBlank(message = "Question text is required")
        private String questionText;
        
        @NotEmpty(message = "Options are required")
        private List<String> options;
        
        @NotNull(message = "Correct answer index is required")
        @Min(value = 0, message = "Correct answer index must be positive")
        private Integer correctAnswerIndex;
    }
}