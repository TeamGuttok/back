package com.app.guttokback.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Message {
    EXAMPLE("예시");

    private final String message;
}
