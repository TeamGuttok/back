package com.app.guttokback.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<CustomExceptionResponse> responseBindException(BindException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new CustomExceptionResponse(httpStatus, "BAD_REQUEST_ERROR", Objects.requireNonNull(exception.getFieldError()).getDefaultMessage()),
                httpStatus
        );
    }

    @ExceptionHandler(CustomApplicationException.class)
    public ResponseEntity<CustomExceptionResponse> handleCustomApplicationException(CustomApplicationException exception) {
        CustomExceptionResponse errorResponse = new CustomExceptionResponse(
                exception.getErrorCode().getHttpStatus(),
                exception.getErrorCode().getErrorCode(),
                exception.getErrorCode().getMessage()
        );
        return new ResponseEntity<>(errorResponse, exception.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomExceptionResponse> requestHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        InvalidFormatException invalidFormatException = (InvalidFormatException) exception.getCause();
        String fieldName = invalidFormatException.getPath().getFirst().getFieldName();
        String targetType = invalidFormatException.getTargetType().getSimpleName();

        String errorMessage = String.format(ErrorCode.INVALID_JSON_INPUT.getMessage(), fieldName, targetType);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                new CustomExceptionResponse(httpStatus, ErrorCode.INVALID_JSON_INPUT.getErrorCode(), Objects.requireNonNull(errorMessage)), httpStatus);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomExceptionResponse> responseNoResourceFoundException() {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        CustomExceptionResponse errorResult = new CustomExceptionResponse(httpStatus, "ENDPOINT_ERROR", "잘못된 요청입니다.");
        return new ResponseEntity<>(errorResult, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionResponse> responseException() {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomExceptionResponse errorResponse = new CustomExceptionResponse(httpStatus, "SERVER_ERROR", "서버 내부 오류가 발생했습니다.");
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
