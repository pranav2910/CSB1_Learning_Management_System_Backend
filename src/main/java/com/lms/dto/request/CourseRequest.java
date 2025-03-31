package com.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Category ID is required")
    private String categoryId;

    @NotEmpty(message = "At least one learning objective is required")
    private List<String> learningObjectives;

    private String thumbnailUrl;
    private String previewVideoUrl;
    private Boolean isPublished = false;
}