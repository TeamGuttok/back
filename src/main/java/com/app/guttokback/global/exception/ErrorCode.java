package com.app.guttokback.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // user
    ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다"),
    EMAIL_SAME_FOUND(HttpStatus.CONFLICT, "중복된 회원이 존재합니다"),

    // userSubscription
    USER_SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 구독항목을 찾을 수 없습니다."),
    MISSING_TITLE(HttpStatus.BAD_REQUEST, "직접 입력 시 제목은 필수 입력입니다."),
    INVALID_INPUT_TITLE(HttpStatus.BAD_REQUEST, "직접 입력 시에만 title 입력이 가능합니다."),

    // auth
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다"),
    SESSION_PROBLEM(HttpStatus.UNAUTHORIZED, "세션이 없거나 만료됐습니다"),
    USER_ACCESS_PROBLEM(HttpStatus.FORBIDDEN, "리소스에 접근할 권한이 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
