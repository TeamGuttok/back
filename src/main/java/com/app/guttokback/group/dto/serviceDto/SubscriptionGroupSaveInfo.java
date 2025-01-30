package com.app.guttokback.group.dto.serviceDto;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionGroupSaveInfo {

    private String email;

    private String title;

    private Subscription subscription;

    private long paymentAmount;

    private PaymentMethod paymentMethod;

    private PaymentCycle paymentCycle;

    private int paymentDay;

    private String notice;

    @Builder
    public SubscriptionGroupSaveInfo(String email,
                                     String title,
                                     Subscription subscription,
                                     long paymentAmount,
                                     PaymentMethod paymentMethod,
                                     PaymentCycle paymentCycle,
                                     int paymentDay,
                                     String notice
    ) {
        this.email = email;
        this.title = title;
        this.subscription = subscription;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.notice = notice;
    }
}
