package com.app.guttokback.global.aws.ses.service;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @AfterEach
    public void clear() {
        userSubscriptionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    private UserEntity createUser(String email, boolean alarm) {
        UserEntity user = UserEntity.builder()
                .email(email)
                .password("!a1234567890")
                .nickName("test")
                .alarm(alarm)
                .build();
        return userRepository.save(user);
    }

    private UserSubscriptionEntity createUserSubscription(UserEntity user) {
        UserSubscriptionEntity userSubscription = new UserSubscriptionEntity(
                user,
                "test",
                Subscription.CUSTOM_INPUT,
                10000,
                PaymentMethod.CARD,
                PaymentCycle.MONTHLY,
                1,
                "test");
        return userSubscriptionRepository.save(userSubscription);
    }

    @Test
    @DisplayName("sendReminder가 유저의 구독 데이터를 기반으로 이메일을 보내고 ReminderDate를 업데이트한다.")
    void sendReminder() {
        // given
        UserEntity user = createUser("test@test.com", true);

        UserSubscriptionEntity userSubscription = createUserSubscription(user);
        reminderService.initializeReminder(userSubscription);

        LocalDate now = LocalDate.now();

        // when
        reminderService.sendReminder(now);

        // then
        assertThat(userSubscription.getReminderDate()).isNotEqualTo(now);
        assertThat(userSubscription.getReminderDate()).isAfter(now);
    }

    @Test
    @DisplayName("유저의 모든 구독 정보에 대해 알림 날짜가 정상적으로 업데이트된다.")
    public void updateAllRemindersForUserTest() {
        // given
        UserEntity user = createUser("test1@test.com", true);
        createUserSubscription(user);
        createUserSubscription(user);

        // when
        reminderService.updateAllRemindersForUser(user.getId());

        // then
        List<UserSubscriptionEntity> updatedSubscriptions = userSubscriptionRepository.findAllByUserId(user.getId());
        assertThat(updatedSubscriptions).hasSize(2);
        assertThat(updatedSubscriptions.get(0).getReminderDate()).isNotNull();
        assertThat(updatedSubscriptions.get(1).getReminderDate()).isNotNull();
    }

    @Test
    @DisplayName("유저의 알림여부가 true일 때, 결제일, 결제주기가 수정 될 경우 알림 날짜가 정상적으로 업데이트된다.")
    public void updateReminderIfPaymentDetailsChangedTest() {
        // given
        UserEntity user = createUser("test2@test.com", true);
        UserSubscriptionEntity userSubscription = createUserSubscription(user);

        LocalDate previousReminderDate = userSubscription.getReminderDate();
        int previousPaymentDay = userSubscription.getPaymentDay();
        PaymentCycle previousPaymentCycle = userSubscription.getPaymentCycle();

        userSubscription.update
                (
                        userSubscription.getTitle(),
                        userSubscription.getPaymentAmount(),
                        userSubscription.getPaymentMethod(),
                        userSubscription.getPaymentStatus(),
                        PaymentCycle.YEARLY,
                        userSubscription.getPaymentDay() + 1,
                        userSubscription.getMemo()
                );

        // when
        reminderService.updateReminderIfPaymentDetailsChanged(userSubscription, previousPaymentDay, previousPaymentCycle);

        // then
        assertThat(userSubscription.getPaymentDay()).isNotEqualTo(previousPaymentDay);
        assertThat(userSubscription.getReminderDate()).isNotNull();
        assertThat(userSubscription.getReminderDate()).isNotEqualTo(previousReminderDate);
    }

    @Test
    @DisplayName("유저의 알림여부가 false일 때, 결제일, 결제주기가 수정되어도 알림 날짜가 업데이트 되지 않는다.")
    public void updateReminderIfPaymentDetailsChangedWithNoAlarmTest() {
        // given
        UserEntity user = createUser("test3@test.com", false);
        UserSubscriptionEntity userSubscription = createUserSubscription(user);

        LocalDate previousReminderDate = userSubscription.getReminderDate();
        int previousPaymentDay = userSubscription.getPaymentDay();
        PaymentCycle previousPaymentCycle = userSubscription.getPaymentCycle();

        userSubscription.update
                (
                        userSubscription.getTitle(),
                        userSubscription.getPaymentAmount(),
                        userSubscription.getPaymentMethod(),
                        userSubscription.getPaymentStatus(),
                        PaymentCycle.YEARLY,
                        userSubscription.getPaymentDay() + 1,
                        userSubscription.getMemo()
                );

        // when
        reminderService.updateReminderIfPaymentDetailsChanged(userSubscription, previousPaymentDay, previousPaymentCycle);

        // then
        assertThat(userSubscription.getPaymentDay()).isNotEqualTo(previousPaymentDay);
        assertThat(userSubscription.getReminderDate()).isEqualTo(previousReminderDate);
        assertThat(userSubscription.getReminderDate()).isNull();
    }

    @Test
    @DisplayName("사용자가 구독항목 생성 시 알림이 true일 경우 일림 날짜도 함께 생성된다.")
    public void initializeReminderTest() {
        // given
        UserEntity user = createUser("test4@test.com", true);
        UserSubscriptionEntity userSubscription = createUserSubscription(user);

        // when
        reminderService.initializeReminder(userSubscription);

        // then
        assertThat(user.isAlarm()).isTrue();
        assertThat(userSubscription.getReminderDate()).isNotNull();
    }

    @Test
    @DisplayName("사용자가 구독항목 생성 시 알림이 false일 경우 일림 날짜가 생성되지 않는다.")
    public void initializeReminderWithNoAlarmTest() {
        // given
        UserEntity user = createUser("test5@test.com", false);
        UserSubscriptionEntity userSubscription = createUserSubscription(user);

        // when
        reminderService.initializeReminder(userSubscription);

        // then
        assertThat(user.isAlarm()).isFalse();
        assertThat(userSubscription.getReminderDate()).isNull();
    }

}
