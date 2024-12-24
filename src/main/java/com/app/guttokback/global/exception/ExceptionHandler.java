package com.app.guttokback.global.exception;

import lombok.Getter;

public class ExceptionHandler extends RuntimeException{
    @Getter
    private final ErrorCode errorCode;

    public ExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
