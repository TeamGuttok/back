package com.app.guttokback.subscription.dto.serviceDto;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.PaymentStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionUpdateInfo {

    private String email;

    private String title;

    private long paymentAmount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private PaymentCycle paymentCycle;

    private int paymentDay;

    private String memo;

    @Builder
    public UserSubscriptionUpdateInfo(String email,
                                      String title,
                                      long paymentAmount,
                                      PaymentMethod paymentMethod,
                                      PaymentStatus paymentStatus,
                                      PaymentCycle paymentCycle,
                                      int paymentDay,
                                      String memo
    ) {
        this.email = email;
        this.title = title;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.memo = memo;
    }
}
