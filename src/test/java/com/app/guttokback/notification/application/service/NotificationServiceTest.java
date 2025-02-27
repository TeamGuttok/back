package com.app.guttokback.notification.application.service;

import com.app.guttokback.common.api.PageOption;
import com.app.guttokback.common.api.PageResponse;
import com.app.guttokback.notification.application.dto.NotificationListResponse;
import com.app.guttokback.notification.domain.entity.Notification;
import com.app.guttokback.notification.domain.enums.Category;
import com.app.guttokback.notification.domain.enums.Message;
import com.app.guttokback.notification.domain.enums.Status;
import com.app.guttokback.notification.domain.repository.NotificationRepository;
import com.app.guttokback.user.domain.entity.User;
import com.app.guttokback.user.domain.repository.UserRepository;
import com.app.guttokback.userSubscription.domain.entity.UserSubscription;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.PaymentMethod;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private User createUser(String email) {
        User user = User.builder()
                .email(email)
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        return userRepository.save(user);
    }

    private UserSubscription createUserSubscription(User user) {
        UserSubscription userSubscription = new UserSubscription(
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

    private Notification createNotification(User user, Status status) {
        Notification notification = new Notification(
                user,
                Category.APPLICATION,
                "test",
                status
        );
        return notificationRepository.save(notification);
    }

    @Test
    @Transactional
    @DisplayName("회원가입 시 정상적으로 환영 알림이 읽지않음 상태로 저장된다.")
    public void userSaveNotificationTest() {
        // given
        User user = createUser("test@gmail.com");
        String message = String.format(Message.USER_SIGNUP_WELCOME.getMessage(), user.getNickName());

        // when
        notificationService.userSaveNotification(user);

        // then
        Notification notification = notificationRepository.findAll().getFirst();
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
        User user = createUser("test1@gmail.com");
        UserSubscription userSubscription = createUserSubscription(user);

        String subscription = userSubscription.getSubscription() == Subscription.CUSTOM_INPUT
                ? userSubscription.getTitle() : userSubscription.getSubscription().getName();

        String message = String.format(
                Message.USER_PAYMENT_REMINDER.getMessage(), user.getNickName(), subscription
        );

        // when
        notificationService.reminderNotification(user, userSubscription);

        // then
        Notification notification = notificationRepository.findAll().getFirst();
        assertThat(notification.getUser()).isEqualTo(user);
        assertThat(notification.getCategory()).isEqualTo(Category.REMINDER);
        assertThat(notification.getMessage()).isEqualTo(message);
        assertThat(notification.getStatus()).isEqualTo(Status.UNREAD);
    }

    @Test
    @DisplayName("존재하는 회원에 대해 생성된 알림이 조회된다.")
    public void notificationListTest() {
        // given
        User user = createUser("test2@test.com");
        Notification notification = createNotification(user, Status.UNREAD);

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
        User user = createUser("test3@test.com");
        Notification savedNotification = createNotification(user, Status.UNREAD);

        // when
        notificationService.statusUpdate(user.getEmail());

        // then
        Notification notification = notificationRepository.findAll().getFirst();
        assertThat(savedNotification.getStatus()).isNotEqualTo(notification.getStatus());
        assertThat(notification.getStatus()).isEqualTo(Status.READ);
    }

    @Test
    @DisplayName("회원에 대한 알림이 읽음 처리된 것에 대한 삭제가 정상적으로 처리된다.")
    public void notificationDeleteTest() {
        // given
        User user = createUser("test4@test.com");
        createNotification(user, Status.READ);

        // when
        notificationService.delete(user.getEmail());

        // then
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).isEmpty();
    }

}
