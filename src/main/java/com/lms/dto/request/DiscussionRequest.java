package com.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DiscussionRequest {
    @NotBlank(message = "Content cannot be empty")
    private String content;
    
    // For replies
    private String parentId; // ID of the parent discussion (for replies)
}