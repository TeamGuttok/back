package com.app.guttokback.subscription.dto.controllerDto.request;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionUpdateInfo;
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
public class UserSubscriptionUpdateRequest {

    private String title;

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
    public UserSubscriptionUpdateRequest(String title,
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

    public UserSubscriptionUpdateInfo toUpdate() {
        return UserSubscriptionUpdateInfo.builder()
                .title(title)
                .paymentAmount(paymentAmount)
                .paymentMethod(paymentMethod)
                .startDate(startDate)
                .paymentCycle(paymentCycle)
                .paymentDay(paymentDay)
                .memo(memo)
                .build();
    }
}
