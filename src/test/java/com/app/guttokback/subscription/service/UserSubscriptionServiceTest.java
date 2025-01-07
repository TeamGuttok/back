package com.app.guttokback.subscription.service;

import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.subscription.dto.serviceDto.SubscriptionListInfo;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionListInfo;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionUpdateInfo;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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

    @AfterEach
    public void clear() {
        userSubscriptionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
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

    private UserSubscriptionEntity createUserSubscription() {
        UserEntity user = createUser();

        UserSubscriptionEntity userSubscription = new UserSubscriptionEntity(
                user,
                "test",
                Subscription.CUSTOM_INPUT,
                10000,
                PaymentMethod.CARD,
                LocalDate.parse("2025-01-01"),
                PaymentCycle.MONTHLY,
                1,
                "test");
        return userSubscriptionRepository.save(userSubscription);
    }

    @Test
    @DisplayName("존재하는 회원이 구독항목 생성 시 구독항목이 정상적으로 저장된다.")
    public void savedUserSubscriptionTest() {
        // given
        UserEntity savedUser = createUser();

        UserSubscriptionSaveInfo savedUserSubscription = UserSubscriptionSaveInfo.builder()
                .userId(savedUser.getId())
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
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
        UserSubscriptionSaveInfo savedUserSubscription = UserSubscriptionSaveInfo.builder()
                .userId(-1L)
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
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
    @DisplayName("존재하는 회원이 생성한 구독항목에 대하여 조회된다.")
    public void userSubscriptionListPageTest() {
        // given
        UserEntity savedUser = createUser();

        UserSubscriptionEntity userSubscription = UserSubscriptionEntity.builder()
                .user(savedUser)
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
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

    @Test
    @DisplayName("존재하는 구독항목 수정 시 정상적으로 수정된다.")
    public void userSubscriptionUpdateTest() {
        // given
        UserSubscriptionEntity savedUserSubscription = createUserSubscription();

        UserSubscriptionUpdateInfo userSubscriptionUpdateInfo
                = new UserSubscriptionUpdateInfo(
                "update",
                120000,
                PaymentMethod.BANK_TRANSFER,
                LocalDate.parse("2024-12-31"),
                PaymentCycle.YEARLY,
                15,
                "test"
        );

        // when
        userSubscriptionService.update(savedUserSubscription.getId(), userSubscriptionUpdateInfo);

        // then
        UserSubscriptionEntity userSubscription = userSubscriptionRepository.findAll().stream().findFirst().orElseThrow();
        assertThat(userSubscription.getTitle()).isEqualTo("update");
        assertThat(userSubscription.getPaymentAmount()).isEqualTo(120000);
        assertThat(userSubscription.getPaymentMethod()).isEqualTo(PaymentMethod.BANK_TRANSFER);
        assertThat(userSubscription.getStartDate()).isEqualTo(LocalDate.parse("2024-12-31"));
        assertThat(userSubscription.getPaymentCycle()).isEqualTo(PaymentCycle.YEARLY);
        assertThat(userSubscription.getPaymentDay()).isEqualTo(15);
        assertThat(userSubscription.getMemo()).isEqualTo("test");
    }

    @Test
    @DisplayName("사용자 구독항목이 존재하지 않을 시 예외가 발생한다.")
    public void userSubscriptionUpdateValidateTest() {
        // given
        UserSubscriptionUpdateInfo userSubscriptionUpdateInfo
                = new UserSubscriptionUpdateInfo(
                "update",
                120000,
                PaymentMethod.BANK_TRANSFER,
                LocalDate.parse("2024-12-31"),
                PaymentCycle.YEARLY,
                15,
                "test"
        );

        // when
        CustomApplicationException exception = assertThrows(CustomApplicationException.class,
                () -> userSubscriptionService.update(-1L, userSubscriptionUpdateInfo));

        // then
        assertThat(exception).isInstanceOf(CustomApplicationException.class);
        assertThat(exception.getMessage()).isEqualTo("사용자의 구독항목을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("존재하는 구독항목 삭제 시 정상적으로 삭제된다.")
    public void userSubscriptionDeleteTest() {
        // given
        UserSubscriptionEntity userSubscription = createUserSubscription();

        // when
        userSubscriptionService.delete(userSubscription.getId());

        // then
        assertThat(userSubscriptionRepository.findById(userSubscription.getId())).isEmpty();
    }

    @Test
    @DisplayName("구독 서비스 조회 시 Subscription 클래스가 응답된다.")
    public void subscriptionListTest() {
        // given
        List<SubscriptionListInfo> subscriptions = Arrays.stream(Subscription.values())
                .map(subscription -> new SubscriptionListInfo(subscription.name(), subscription.getName()))
                .toList();

        // when
        List<SubscriptionListInfo> subscriptionList = userSubscriptionService.subscriptionList();

        // then
        assertThat(subscriptionList).isNotNull();
        assertThat(subscriptionList).hasSize(Subscription.values().length);
        for (int i = 0; i < subscriptions.size(); i++) {
            SubscriptionListInfo expected = subscriptions.get(i);
            SubscriptionListInfo actual = subscriptionList.get(i);

            assertThat(actual.getCode()).isEqualTo(expected.getCode());
            assertThat(actual.getName()).isEqualTo(expected.getName());
        }
    }
}
