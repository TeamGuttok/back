package com.app.guttokback.subscription.dto.controllerDto.response;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
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

    private String subscription;

    private long paymentAmount;

    private PaymentMethod paymentMethod;

    private PaymentCycle paymentCycle;

    private int paymentDay;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime registerDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateDate;

    @Builder
    public UserSubscriptionListResponse(Long id,
                                        String title,
                                        String subscription,
                                        long paymentAmount,
                                        PaymentMethod paymentMethod,
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
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    public static UserSubscriptionListResponse of(UserSubscriptionEntity userSubscriptionEntity) {
        return UserSubscriptionListResponse.builder()
                .id(userSubscriptionEntity.getId())
                .title(userSubscriptionEntity.getTitle())
                .subscription(userSubscriptionEntity.getSubscription().getName())
                .paymentAmount(userSubscriptionEntity.getPaymentAmount())
                .paymentMethod(userSubscriptionEntity.getPaymentMethod())
                .paymentCycle(userSubscriptionEntity.getPaymentCycle())
                .paymentDay(userSubscriptionEntity.getPaymentDay())
                .registerDate(userSubscriptionEntity.getRegisterDate())
                .updateDate(userSubscriptionEntity.getUpdateDate())
                .build();
    }
}
