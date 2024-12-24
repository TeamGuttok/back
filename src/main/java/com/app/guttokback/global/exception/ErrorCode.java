package com.app.guttokback.global.exception;

import lombok.Getter;

public enum ErrorCode {
    EMAIL_NOT_FOUND("해당 Email을 찾을 수 없습니다."),
    EMAIL_SAME_FOUND("동일한 Email이 존재합니다");


    @Getter
    private String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
