package com.app.guttokback.notification.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    READ("읽음"),
    UNREAD("읽지 않음");

    private final String status;
}
