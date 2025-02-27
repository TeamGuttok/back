package com.app.guttokback.userSubscription.application.dto.serviceDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionSearchInfo {

    private String name;

    @Builder
    public SubscriptionSearchInfo(String name) {
        this.name = name;
    }

}
