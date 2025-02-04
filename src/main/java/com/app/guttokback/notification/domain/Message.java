package com.app.guttokback.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Message {
    USER_SIGNUP_WELCOME("%s님, 회원이 되신 것을 진심으로 환영합니다 🎉"),
    USER_PAYMENT_REMINDER("%s님, 내일은 %s 결제일 입니다 💰");

    private final String message;
}
