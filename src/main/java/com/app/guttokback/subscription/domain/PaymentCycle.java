package com.app.guttokback.subscription.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentCycle {
    YEARLY("연"),
    MONTHLY("월"),
    WEEKLY("주");
    
    private final String cycle;
}
