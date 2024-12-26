package com.app.guttokback.global.apiResponse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ApiResponse<T> {
    private String message;
    private T data;
    private HttpStatus status;

    public ApiResponse(String message, T data, HttpStatus status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(message, data, HttpStatus.OK));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        return ResponseEntity.ok(new ApiResponse<>(message, null, HttpStatus.OK));
    }

    public static ResponseEntity<ApiResponse<Object>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(message, null, status));
    }
}
