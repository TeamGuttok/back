package com.app.guttokback.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomApplicationException.class)
    public ResponseEntity<CustomExceptionResponse> handleCustomApplicationException(CustomApplicationException exception) {
        CustomExceptionResponse errorResponse = new CustomExceptionResponse(
                exception.getErrorCode().getHttpStatus(),
                exception.getErrorCode().getErrorCode(),
                exception.getErrorCode().getMessage()
        );
        return new ResponseEntity<>(errorResponse, exception.getErrorCode().getHttpStatus());
    }
}
