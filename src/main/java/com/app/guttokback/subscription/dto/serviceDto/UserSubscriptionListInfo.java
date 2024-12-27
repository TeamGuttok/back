package com.app.guttokback.subscription.dto.serviceDto;

import lombok.Getter;

@Getter
public class UserSubscriptionListInfo {

    private final Long userId;

    private final Long lastId;

    private final long size;

    public UserSubscriptionListInfo(Long userId, Long lastId, long size) {
        this.userId = userId;
        this.lastId = lastId;
        this.size = size;
    }
}
