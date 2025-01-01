package com.app.guttokback.subscription.dto.serviceDto;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionUpdateInfo {

    private String title;

    private long paymentAmount;

    private PaymentMethod paymentMethod;

    private LocalDate startDate;

    private PaymentCycle paymentCycle;

    private int paymentDay;

    private String memo;

    @Builder
    public UserSubscriptionUpdateInfo(String title,
                                      long paymentAmount,
                                      PaymentMethod paymentMethod,
                                      LocalDate startDate,
                                      PaymentCycle paymentCycle,
                                      int paymentDay,
                                      String memo
    ) {
        this.title = title;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.startDate = startDate;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.memo = memo;
    }
}
