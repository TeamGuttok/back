package com.app.guttokback.userSubscription.application.dto.serviceDto;

import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.PaymentMethod;
import com.app.guttokback.userSubscription.domain.enums.PaymentStatus;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionSaveInfo {

    private String email;

    private String title;

    private Subscription subscription;

    private long paymentAmount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private PaymentCycle paymentCycle;

    private int paymentDay;

    private String memo;

    @Builder
    public UserSubscriptionSaveInfo(String email,
                                    String title,
                                    Subscription subscription,
                                    long paymentAmount,
                                    PaymentMethod paymentMethod,
                                    PaymentStatus paymentStatus,
                                    PaymentCycle paymentCycle,
                                    int paymentDay,
                                    String memo
    ) {
        this.email = email;
        this.title = title;
        this.subscription = subscription;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.memo = memo;
    }
}
