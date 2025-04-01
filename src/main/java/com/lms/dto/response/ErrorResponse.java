package com.lms.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;
    private String path;

    public ErrorResponse(LocalDateTime timestamp, HttpStatus status, String message, List<String> errors, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.path = path;
    }
}
