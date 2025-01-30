package com.app.guttokback.group.dto.controllerDto;

import com.app.guttokback.group.dto.serviceDto.SubscriptionGroupSaveInfo;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionGroupSaveRequest {

    @NotBlank(message = "그룹명을 입력하세요.")
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

    private String notice;

    @Builder
    public SubscriptionGroupSaveRequest(String title,
                                        Subscription subscription,
                                        long paymentAmount,
                                        PaymentMethod paymentMethod,
                                        PaymentCycle paymentCycle,
                                        int paymentDay,
                                        String notice
    ) {
        this.title = title;
        this.subscription = subscription;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.notice = notice;
    }

    public SubscriptionGroupSaveInfo toSave(String email) {
        return SubscriptionGroupSaveInfo.builder()
                .email(email)
                .title(title)
                .subscription(subscription)
                .paymentAmount(paymentAmount)
                .paymentMethod(paymentMethod)
                .paymentCycle(paymentCycle)
                .paymentDay(paymentDay)
                .notice(notice)
                .build();
    }
}
