package com.app.guttokback.subscription.dto.serviceDto;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSubscriptionSaveInfo {

    private final Long userId;

    private final String title;
    private final Long subscriptionId;

    private final long paymentAmount;

    private final PaymentMethod paymentMethod;

    private final LocalDate startDate;

    private final PaymentCycle paymentCycle;

    private final int paymentDay;

    private final String memo;

    public UserSubscriptionSaveInfo(Long userId,
                                    String title,
                                    Long subscriptionId,
                                    long paymentAmount,
                                    PaymentMethod paymentMethod,
                                    LocalDate startDate,
                                    PaymentCycle paymentCycle,
                                    int paymentDay,
                                    String memo
    ) {
        this.userId = userId;
        this.title = title;
        this.subscriptionId = subscriptionId;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.startDate = startDate;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.memo = memo;
    }
}
