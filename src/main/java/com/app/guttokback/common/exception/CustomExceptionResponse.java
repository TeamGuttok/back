package com.app.guttokback.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomExceptionResponse {
    private final int code;
    private final String errorCode;
    private final String message;

    public CustomExceptionResponse(HttpStatusCode status, String errorCode, String message) {
        this.code = status.value();
        this.errorCode = errorCode;
        this.message = message;
    }
}
