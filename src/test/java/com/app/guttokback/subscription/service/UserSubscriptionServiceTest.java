package com.app.guttokback.subscription.service;

import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.SubscriptionEntity;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionListInfo;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import com.app.guttokback.subscription.repository.SubscriptionRepository;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
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

    private UserEntity createUser() {
        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        return userRepository.save(user);
    }

    private SubscriptionEntity createSubscription() {
        SubscriptionEntity subscription = new SubscriptionEntity("test");
        return subscriptionRepository.save(subscription);
    }

    @Test
    @DisplayName("존재하는 회원이 구독항목 생성 시 구독항목이 정상적으로 저장된다.")
    public void savedUserSubscriptionTest() {
        // given
        UserEntity savedUser = createUser();
        SubscriptionEntity savedSubscription = createSubscription();

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
    @DisplayName("존재하지 않는 회원이 구독항목 생성 시 예외가 발생한다.")
    public void saveUserSubscriptionByValidateUserTest() {
        // given
        SubscriptionEntity savedSubscription = createSubscription();

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
        assertThat(exception)
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage("회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("구독서비스가 존재하지 않을 경우 구독항목 생성 시 예외가 발생한다.")
    public void saveUserSubscriptionByValidateSubscriptionTest() {
        // given
        UserEntity savedUser = createUser();

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
        assertThat(exception)
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage("구독 서비스를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("존재하는 회원이 생성한 구독항목에 대하여 조회된다.")
    public void userSubscriptionListPageTest() {
        // given
        UserEntity savedUser = createUser();

        SubscriptionEntity savedSubscription = createSubscription();

        UserSubscriptionEntity userSubscription = UserSubscriptionEntity.builder()
                .user(savedUser)
                .title("test")
                .subscription(savedSubscription)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .startDate(LocalDate.parse("2024-12-27"))
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();
        UserSubscriptionEntity savedUserSubscription = userSubscriptionRepository.save(userSubscription);

        UserSubscriptionListInfo userSubscriptionListInfo = new UserSubscriptionListInfo(
                savedUser.getId(), null, 5
        );

        // when
        PageResponse<UserSubscriptionListResponse> list = userSubscriptionService.list(userSubscriptionListInfo);

        // then
        assertThat(list.getContents())
                .hasSize(1)
                .extracting(UserSubscriptionListResponse::getId)
                .containsExactly(savedUserSubscription.getId());
        assertThat(list.getContents()).extracting(UserSubscriptionListResponse::getTitle)
                .containsExactly(userSubscription.getTitle());
        assertThat(list.getContents()).extracting(UserSubscriptionListResponse::getPaymentAmount)
                .containsExactly(userSubscription.getPaymentAmount());
        assertThat(list.getContents()).extracting(UserSubscriptionListResponse::getPaymentMethod)
                .containsExactly(userSubscription.getPaymentMethod());
        assertThat(list.getContents()).extracting(UserSubscriptionListResponse::getPaymentCycle)
                .containsExactly(userSubscription.getPaymentCycle());
        assertThat(list.getContents()).extracting(UserSubscriptionListResponse::getPaymentDay)
                .containsExactly(userSubscription.getPaymentDay());
    }

    @Test
    @DisplayName("존재하지 않는 회원이 구독항목 조회 시 예외가 발생한다.")
    public void userSubscriptionListPageUserValidateTest() {
        // given
        UserSubscriptionListInfo userSubscriptionListInfo = new UserSubscriptionListInfo(
                -1L, null, 5
        );

        // when
        CustomApplicationException exception = assertThrows(CustomApplicationException.class,
                () -> userSubscriptionService.list(userSubscriptionListInfo));

        // then
        assertThat(exception).isInstanceOf(CustomApplicationException.class);
        assertThat(exception.getMessage()).isEqualTo("회원을 찾을 수 없습니다");
    }
}
