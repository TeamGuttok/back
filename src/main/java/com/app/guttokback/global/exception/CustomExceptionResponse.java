package com.app.guttokback.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomExceptionResponse {
    private final int code;
    private final String message;

    public CustomExceptionResponse(HttpStatusCode status, String message) {
        this.code = status.value();
        this.message = message;
    }
}
