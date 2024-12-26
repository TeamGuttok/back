package com.app.guttokback.subscription.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    CARD("카드"),
    BANK_TRANSFER("계좌이체");

    private final String method;
}
