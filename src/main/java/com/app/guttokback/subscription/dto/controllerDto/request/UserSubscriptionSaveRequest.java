package com.app.guttokback.subscription.dto.controllerDto.request;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionSaveRequest {

    @NotNull(message = "회원 ID를 입력하세요.")
    @Positive(message = "회원 ID는 양수여야 합니다.")
    private Long userId;

    private String title;

    @NotNull(message = "구독 서비스 ID를 입력하세요.")
    @Positive(message = "구독 서비스 ID는 양수여야 합니다.")
    private Long subscriptionId;

    @Positive(message = "납부금액은 양수여야 합니다.")
    private long paymentAmount;

    @NotNull(message = "결제수단을 선택하세요.")
    private PaymentMethod paymentMethod;

    @NotNull(message = "첫 납부 날짜를 입력하세요.")
    private LocalDate startDate;

    @NotNull(message = "결제주기를 선택하세요.")
    private PaymentCycle paymentCycle;

    @Min(value = 1, message = "결제일자는 1 이상이어야 합니다.")
    @Max(value = 31, message = "결제일자는 31 이하이어야 합니다.")
    private int paymentDay;

    private String memo;

    @Builder
    public UserSubscriptionSaveRequest(Long userId,
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

    public UserSubscriptionSaveInfo toSave() {
        return UserSubscriptionSaveInfo.builder()
                .userId(userId)
                .title(title)
                .subscriptionId(subscriptionId)
                .paymentAmount(paymentAmount)
                .paymentMethod(paymentMethod)
                .startDate(startDate)
                .paymentCycle(paymentCycle)
                .paymentDay(paymentDay)
                .memo(memo)
                .build();
    }

}
