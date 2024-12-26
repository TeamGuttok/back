package com.app.guttokback.subscription.dto.controllerDto.request;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class UserSubscriptionSaveRequest {

    @NotNull(message = "회원 ID를 입력하세요.")
    @Positive(message = "회원 ID는 양수여야 합니다.")
    private Long userId;

    private String title;

    @NotNull(message = "구독 서비스 ID를 입력하세요.")
    @Positive(message = "구독 서비스 ID는 양수여야 합니다.")
    private Long subscriptionId;

    @NotNull(message = "납부금액을 입력하세요.")
    @Positive(message = "납부금액은 양수여야 합니다.")
    private long paymentAmount;

    @NotNull(message = "결제수단을 선택하세요.")
    private PaymentMethod paymentMethod;

    @NotNull(message = "첫 납부 날짜를 입력하세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "결제주기를 입력하세요.")
    private PaymentCycle paymentCycle;

    @NotNull(message = "결제일자를 입력하세요.")
    @Min(value = 1, message = "결제일자는 1 이상이어야 합니다.")
    @Max(value = 31, message = "결제일자는 31 이하이어야 합니다.")
    private int paymentDay;

    private String memo;

    public UserSubscriptionSaveInfo toSave() {
        return new UserSubscriptionSaveInfo(
                userId,
                title,
                subscriptionId,
                paymentAmount,
                paymentMethod,
                startDate,
                paymentCycle,
                paymentDay,
                memo
        );
    }

}
