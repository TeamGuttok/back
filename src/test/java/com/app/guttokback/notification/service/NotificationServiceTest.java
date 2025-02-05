package com.app.guttokback.notification.service;

import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.apiResponse.util.PageOption;
import com.app.guttokback.notification.domain.Category;
import com.app.guttokback.notification.domain.Message;
import com.app.guttokback.notification.domain.NotificationEntity;
import com.app.guttokback.notification.domain.Status;
import com.app.guttokback.notification.dto.controllerDto.response.NotificationListResponse;
import com.app.guttokback.notification.repository.NotificationRepository;
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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

    @AfterEach
    public void clear() {
        notificationRepository.deleteAllInBatch();
        userSubscriptionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    private UserEntity createUser(String email) {
        UserEntity user = UserEntity.builder()
                .email(email)
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
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

    private NotificationEntity createNotification(UserEntity user) {
        NotificationEntity notification = new NotificationEntity(
                user,
                Category.APPLICATION,
                "test",
                Status.UNREAD
        );
        return notificationRepository.save(notification);
    }

    @Test
    @Transactional
    @DisplayName("회원가입 시 정상적으로 환영 알림이 읽지않음 상태로 저장된다.")
    public void userSaveNotificationTest() {
        // given
        UserEntity user = createUser("test@gmail.com");
        String message = String.format(Message.USER_SIGNUP_WELCOME.getMessage(), user.getNickName());

        // when
        notificationService.userSaveNotification(user);

        // then
        NotificationEntity notification = notificationRepository.findAll().getFirst();
        assertThat(notification.getUser()).isEqualTo(user);
        assertThat(notification.getCategory()).isEqualTo(Category.APPLICATION);
        assertThat(notification.getMessage()).isEqualTo(message);
        assertThat(notification.getStatus()).isEqualTo(Status.UNREAD);
    }

    @Test
    @Transactional
    @DisplayName("구독 항목에 대한 결제일 알림이 읽지않음 상태로 저장된다.")
    public void reminderNotificationTest() {
        // given
        UserEntity user = createUser("test1@gmail.com");
        UserSubscriptionEntity userSubscription = createUserSubscription(user);

        String subscription = userSubscription.getSubscription() == Subscription.CUSTOM_INPUT
                ? userSubscription.getTitle() : userSubscription.getSubscription().getName();

        String message = String.format(
                Message.USER_PAYMENT_REMINDER.getMessage(), user.getNickName(), subscription
        );

        // when
        notificationService.reminderNotification(user, userSubscription);

        // then
        NotificationEntity notification = notificationRepository.findAll().getFirst();
        assertThat(notification.getUser()).isEqualTo(user);
        assertThat(notification.getCategory()).isEqualTo(Category.REMINDER);
        assertThat(notification.getMessage()).isEqualTo(message);
        assertThat(notification.getStatus()).isEqualTo(Status.UNREAD);
    }

    @Test
    @DisplayName("존재하는 회원에 대해 생성된 알림이 조회된다.")
    public void notificationListTest() {
        // given
        UserEntity user = createUser("test2@test.com");
        NotificationEntity notification = createNotification(user);

        PageOption pageOption = new PageOption(
                user.getEmail(), null, 5
        );

        // when
        PageResponse<NotificationListResponse> list = notificationService.list(pageOption);

        // then
        assertThat(list.getContents())
                .hasSize(1)
                .extracting(NotificationListResponse::getId)
                .containsExactly(notification.getId());
        assertThat(list.getContents()).extracting(NotificationListResponse::getCategory).containsExactly(Category.APPLICATION);
        assertThat(list.getContents()).extracting(NotificationListResponse::getMessage).containsExactly("test");
        assertThat(list.getContents()).extracting(NotificationListResponse::getStatus).containsExactly(Status.UNREAD);
    }

    @Test
    @DisplayName("회원에 대한 알림이 정상적으로 읽음 처리 된다.")
    public void statusUpdateTest() {
        // given
        UserEntity user = createUser("test3@test.com");
        NotificationEntity savedNotification = createNotification(user);

        // when
        notificationService.statusUpdate(user.getEmail());

        // then
        NotificationEntity notification = notificationRepository.findAll().getFirst();
        assertThat(savedNotification.getStatus()).isNotEqualTo(notification.getStatus());
        assertThat(notification.getStatus()).isEqualTo(Status.READ);
    }

}
