package com.app.guttokback.global.exception;

import lombok.Getter;

public enum ErrorCode {
    EMAIL_NOT_FOUND("해당 EMAIL을 찾을 수 없습니다.");


    @Getter
    private String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
