package com.app.guttokback.userSubscription.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    COMPLETED("결제 완료"),
    PENDING("미결제");

    private final String status;
}
