package com.lms.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String password;
    private String avatarUrl;
    private Boolean isVerified;
    private String role; // Added this field
}
