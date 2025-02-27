package com.app.guttokback.email.application.dto.serviceDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionTotalAmountInfo {

    private Long userId;
    private Long totalAmount;

    @QueryProjection
    public SubscriptionTotalAmountInfo(Long userId, Long totalAmount) {
        this.userId = userId;
        this.totalAmount = totalAmount;
    }
}
