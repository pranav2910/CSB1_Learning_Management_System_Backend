package com.lms.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {
    private String status;
    private int statusCode;
    private String message;
    private T data;
    private Long timestamp;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status.name();
        this.statusCode = status.value();
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "Success", data);
    }

    public static ApiResponse<?> error(HttpStatus status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}