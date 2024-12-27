package com.app.guttokback.subscription.service;

import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.SubscriptionEntity;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import com.app.guttokback.subscription.repository.SubscriptionRepository;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserSubscriptionServiceTest {

    @Autowired
    private UserSubscriptionService userSubscriptionService;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @AfterEach
    public void clear() {
        userSubscriptionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        subscriptionRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    @DisplayName("존재하는 회원이 구독항목 생성 시 구독항목이 정상적으로 저장된다.")
    public void savedUserSubscriptionTest() {
        // given
        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        UserEntity savedUser = userRepository.save(user);

        SubscriptionEntity subscription = new SubscriptionEntity("test");
        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);

        UserSubscriptionSaveInfo savedUserSubscription = UserSubscriptionSaveInfo.builder()
                .userId(savedUser.getId())
                .title("test")
                .subscriptionId(savedSubscription.getId())
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .startDate(LocalDate.parse("2024-12-27"))
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();

        // when
        userSubscriptionService.save(savedUserSubscription);

        // then
        UserSubscriptionEntity userSubscription = userSubscriptionRepository.findAll().stream().findFirst().orElseThrow();
        assertThat(userSubscription.getTitle()).isEqualTo("test");
        assertThat(userSubscription.getPaymentAmount()).isEqualTo(10000);
        assertThat(userSubscription.getPaymentMethod()).isEqualTo(PaymentMethod.CARD);
        assertThat(userSubscription.getStartDate()).isEqualTo(LocalDate.parse("2024-12-27"));
        assertThat(userSubscription.getPaymentCycle()).isEqualTo(PaymentCycle.MONTHLY);
        assertThat(userSubscription.getPaymentDay()).isEqualTo(15);
        assertThat(userSubscription.getMemo()).isEqualTo("test");
    }

    @Test
    @Transactional
    @DisplayName("존재하지 않는 회원이 구독항목 생성 시 예외가 발생한다.")
    public void saveUserSubscriptionByValidateUserTest() {
        // given
        SubscriptionEntity subscription = new SubscriptionEntity("test");
        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);

        UserSubscriptionSaveInfo savedUserSubscription = UserSubscriptionSaveInfo.builder()
                .userId(-1L)
                .title("test")
                .subscriptionId(savedSubscription.getId())
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .startDate(LocalDate.parse("2024-12-27"))
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();

        // when
        CustomApplicationException exception = assertThrows(CustomApplicationException.class,
                () -> userSubscriptionService.save(savedUserSubscription));

        // then
        Assertions.assertThat(exception)
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage("회원을 찾을 수 없습니다");
    }

    @Test
    @Transactional
    @DisplayName("구독서비스가 존재하지 않을 경우 구독항목 생성 시 예외가 발생한다.")
    public void saveUserSubscriptionByValidateSubscriptionTest() {
        // given
        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        UserEntity savedUser = userRepository.save(user);

        UserSubscriptionSaveInfo savedUserSubscription = UserSubscriptionSaveInfo.builder()
                .userId(savedUser.getId())
                .title("test")
                .subscriptionId(-1L)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .startDate(LocalDate.parse("2024-12-27"))
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();

        // when
        CustomApplicationException exception = assertThrows(CustomApplicationException.class,
                () -> userSubscriptionService.save(savedUserSubscription));

        // then
        Assertions.assertThat(exception)
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage("구독 서비스를 찾을 수 없습니다.");
    }
}
