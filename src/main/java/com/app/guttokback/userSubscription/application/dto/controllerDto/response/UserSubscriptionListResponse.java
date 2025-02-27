package com.app.guttokback.userSubscription.application.dto.controllerDto.response;

import com.app.guttokback.userSubscription.domain.entity.UserSubscription;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.PaymentMethod;
import com.app.guttokback.userSubscription.domain.enums.PaymentStatus;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionListResponse {

    private Long id;

    private String title;

    private Subscription subscription;

    private long paymentAmount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private PaymentCycle paymentCycle;

    private int paymentDay;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime registerDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateDate;

    @Builder
    public UserSubscriptionListResponse(Long id,
                                        String title,
                                        Subscription subscription,
                                        long paymentAmount,
                                        PaymentMethod paymentMethod,
                                        PaymentStatus paymentStatus,
                                        PaymentCycle paymentCycle,
                                        int paymentDay,
                                        LocalDateTime registerDate,
                                        LocalDateTime updateDate

    ) {
        this.id = id;
        this.title = title;
        this.subscription = subscription;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    public static UserSubscriptionListResponse of(UserSubscription userSubscription) {
        return UserSubscriptionListResponse.builder()
                .id(userSubscription.getId())
                .title(userSubscription.getTitle())
                .subscription(userSubscription.getSubscription())
                .paymentAmount(userSubscription.getPaymentAmount())
                .paymentMethod(userSubscription.getPaymentMethod())
                .paymentStatus(userSubscription.getPaymentStatus())
                .paymentCycle(userSubscription.getPaymentCycle())
                .paymentDay(userSubscription.getPaymentDay())
                .registerDate(userSubscription.getRegisterDate())
                .updateDate(userSubscription.getUpdateDate())
                .build();
    }
}
