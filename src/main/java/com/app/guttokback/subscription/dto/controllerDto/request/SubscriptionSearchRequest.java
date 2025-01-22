package com.app.guttokback.subscription.dto.controllerDto.request;

import com.app.guttokback.subscription.dto.serviceDto.SubscriptionSearchInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionSearchRequest {

    private String name;

    @Builder
    public SubscriptionSearchRequest(String name) {
        this.name = name;
    }

    public SubscriptionSearchInfo toSearch() {
        return SubscriptionSearchInfo.builder().name(name).build();
    }
}
