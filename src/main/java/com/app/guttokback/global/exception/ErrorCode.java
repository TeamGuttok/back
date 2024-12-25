package com.app.guttokback.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 Email을 찾을 수 없습니다."),
    EMAIL_SAME_FOUND(HttpStatus.CONFLICT, "동일한 Email이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
