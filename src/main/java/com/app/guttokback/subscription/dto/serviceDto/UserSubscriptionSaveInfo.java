package com.app.guttokback.subscription.dto.serviceDto;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionSaveInfo {

    private Long userId;

    private String title;

    private Subscription subscription;

    private long paymentAmount;

    private PaymentMethod paymentMethod;

    private LocalDate startDate;

    private PaymentCycle paymentCycle;

    private int paymentDay;

    private String memo;

    @Builder
    public UserSubscriptionSaveInfo(Long userId,
                                    String title,
                                    Subscription subscription,
                                    long paymentAmount,
                                    PaymentMethod paymentMethod,
                                    LocalDate startDate,
                                    PaymentCycle paymentCycle,
                                    int paymentDay,
                                    String memo
    ) {
        this.userId = userId;
        this.title = title;
        this.subscription = subscription;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.startDate = startDate;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.memo = memo;
    }
}
