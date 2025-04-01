package com.lms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String role;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private Integer totalCoursesEnrolled;
    private Integer totalCoursesCreated;
    private Boolean isVerified;
}
