package com.app.guttokback.subscription.dto.serviceDto;

import lombok.Getter;

@Getter
public class UserSubscriptionListInfo {

    private final String userEmail;

    private final Long lastId;

    private final long size;

    public UserSubscriptionListInfo(String userEmail, Long lastId, long size) {
        this.userEmail = userEmail;
        this.lastId = lastId;
        this.size = size;
    }
}
