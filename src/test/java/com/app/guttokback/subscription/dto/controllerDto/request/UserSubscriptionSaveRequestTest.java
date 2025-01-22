package com.app.guttokback.subscription.dto.controllerDto.request;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class UserSubscriptionSaveRequestTest {

    @Test
    @DisplayName("구독 서비스 ID가 null 일 경우 예외가 발생한다.")
    public void subscriptionIdIsNullValidationTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserSubscriptionSaveRequest userSubscriptionSaveRequest = UserSubscriptionSaveRequest.builder()
                .title("test")
                .subscription(null)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();

        // when
        Set<ConstraintViolation<UserSubscriptionSaveRequest>> violations = validator.validate(userSubscriptionSaveRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("항목을 선택하세요.")
                .hasSize(1);
    }

    @Test
    @DisplayName("납부금액이 음수 일 경우 예외가 발생한다.")
    public void paymentAmountNegativeValidationTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserSubscriptionSaveRequest userSubscriptionSaveRequest = UserSubscriptionSaveRequest.builder()
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(-1)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();

        // when
        Set<ConstraintViolation<UserSubscriptionSaveRequest>> violations = validator.validate(userSubscriptionSaveRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("납부금액은 양수여야 합니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("결제수단이 null 일 경우 예외가 발생한다.")
    public void paymentMethodIsNullValidationTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserSubscriptionSaveRequest userSubscriptionSaveRequest = UserSubscriptionSaveRequest.builder()
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(null)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();

        // when
        Set<ConstraintViolation<UserSubscriptionSaveRequest>> violations = validator.validate(userSubscriptionSaveRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("결제수단을 선택하세요.")
                .hasSize(1);
    }

    @Test
    @DisplayName("결제주기가 null 일 경우 예외가 발생한다.")
    public void paymentCycleIsNullValidationTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserSubscriptionSaveRequest userSubscriptionSaveRequest = UserSubscriptionSaveRequest.builder()
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(null)
                .paymentDay(15)
                .memo("test")
                .build();

        // when
        Set<ConstraintViolation<UserSubscriptionSaveRequest>> violations = validator.validate(userSubscriptionSaveRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("결제주기를 선택하세요.")
                .hasSize(1);
    }

    @Test
    @DisplayName("결제일자가 1 미만일 경우 예외가 발생한다.")
    public void paymentDayMinValidationTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserSubscriptionSaveRequest userSubscriptionSaveRequest = UserSubscriptionSaveRequest.builder()
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(0)
                .memo("test")
                .build();

        // when
        Set<ConstraintViolation<UserSubscriptionSaveRequest>> violations = validator.validate(userSubscriptionSaveRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("결제일자는 1 이상이어야 합니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("결제일자가 31을 초과할 경우 예외가 발생한다.")
    public void paymentDayMaxValidationTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserSubscriptionSaveRequest userSubscriptionSaveRequest = UserSubscriptionSaveRequest.builder()
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(32)
                .memo("test")
                .build();

        // when
        Set<ConstraintViolation<UserSubscriptionSaveRequest>> violations = validator.validate(userSubscriptionSaveRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("결제일자는 31 이하이어야 합니다.")
                .hasSize(1);
    }

}
