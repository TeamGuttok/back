package com.app.guttokback.subscription.dto.controllerDto.request;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionSaveRequest {

    private String title;

    @NotNull(message = "항목을 선택하세요.")
    private Subscription subscription;

    @Positive(message = "납부금액은 양수여야 합니다.")
    private long paymentAmount;

    @NotNull(message = "결제수단을 선택하세요.")
    private PaymentMethod paymentMethod;

    @NotNull(message = "결제주기를 선택하세요.")
    private PaymentCycle paymentCycle;

    @Min(value = 1, message = "결제일자는 1 이상이어야 합니다.")
    @Max(value = 31, message = "결제일자는 31 이하이어야 합니다.")
    private int paymentDay;

    private String memo;

    @Builder
    public UserSubscriptionSaveRequest(String title,
                                       Subscription subscription,
                                       long paymentAmount,
                                       PaymentMethod paymentMethod,
                                       PaymentCycle paymentCycle,
                                       int paymentDay,
                                       String memo
    ) {
        this.title = title;
        this.subscription = subscription;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.memo = memo;
    }

    public UserSubscriptionSaveInfo toSave(String email) {
        return UserSubscriptionSaveInfo.builder()
                .email(email)
                .title(title)
                .subscription(subscription)
                .paymentAmount(paymentAmount)
                .paymentMethod(paymentMethod)
                .paymentCycle(paymentCycle)
                .paymentDay(paymentDay)
                .memo(memo)
                .build();
    }

}
