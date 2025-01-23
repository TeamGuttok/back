package com.app.guttokback.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // user
    ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_01", "회원을 찾을 수 없습니다"),
    EMAIL_SAME_FOUND(HttpStatus.CONFLICT, "USER_02", "중복된 회원이 존재합니다"),
    NICKNAME_SAME_FOUND(HttpStatus.CONFLICT, "USER_03", "중복된 닉네임이 존재합니다"),

    // userSubscription
    USER_SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "SUBSCRIPTION_01", "사용자의 구독항목을 찾을 수 없습니다."),
    MISSING_TITLE(HttpStatus.BAD_REQUEST, "SUBSCRIPTION_02", "직접 입력 시 제목은 필수 입력입니다."),
    INVALID_INPUT_TITLE(HttpStatus.BAD_REQUEST, "SUBSCRIPTION_03", "직접 입력 시에만 title 입력이 가능합니다."),

    // auth
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_01", "계정을 찾을 수 없습니다"),
    SESSION_PROBLEM(HttpStatus.UNAUTHORIZED, "AUTH_02", "세션이 없거나 만료됐습니다"),
    USER_ACCESS_PROBLEM(HttpStatus.FORBIDDEN, "AUTH_03", "리소스에 접근할 권한이 없습니다"),
    CERTIFICATION_NUMBER_NOT_CORRECT(HttpStatus.UNAUTHORIZED, "AUTH_04", "인증 코드가 올바르지 않습니다"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "AUTH_04", "이 작업을 수행할 권한이 없습니다."),
    INVALID_SESSION(HttpStatus.UNAUTHORIZED, "AUTH_05", "유효하지 않은 세션입니다."),
    EMAIL_NOT_MATCH(HttpStatus.BAD_REQUEST, "AUTH_06", "입력된 이메일과 세션 이메일이 일치하지 않습니다."),

    // email
    OVER_REQUEST_COUNT(HttpStatus.TOO_MANY_REQUESTS, "EMAIL_01", "인증코드 요청 횟수를 초과했습니다");



    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
