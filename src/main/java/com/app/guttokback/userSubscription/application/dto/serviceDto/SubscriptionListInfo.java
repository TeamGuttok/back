package com.app.guttokback.userSubscription.application.dto.serviceDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionListInfo {

    private String code;
    private String name;

    public SubscriptionListInfo(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
