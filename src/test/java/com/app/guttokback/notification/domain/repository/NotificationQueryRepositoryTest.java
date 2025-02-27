package com.app.guttokback.notification.domain.repository;

import com.app.guttokback.common.api.PageOption;
import com.app.guttokback.common.infrastructure.config.QueryDslConfig;
import com.app.guttokback.notification.domain.enums.Category;
import com.app.guttokback.notification.domain.enums.Status;
import com.app.guttokback.notification.domain.model.Notification;
import com.app.guttokback.user.domain.model.User;
import com.app.guttokback.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({NotificationQueryRepository.class, QueryDslConfig.class})
class NotificationQueryRepositoryTest {


    @Autowired
    private NotificationQueryRepository notificationQueryRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clear() {
        notificationRepository.deleteAllInBatch();
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
    @DisplayName("존재하는 회원에 대해 생성된 알림에 대해 조회된다.")
    public void findPageNotificationsTest() {
        // given
        User user = createUser("test@test.com");
        Notification notification = createNotification(user, Status.UNREAD);

        PageOption pageOption = new PageOption(user.getEmail(), null, 5);

        // when
        List<Notification> notifications = notificationQueryRepository.findPageNotifications(pageOption);

        // then
        assertThat(notifications).hasSize(1);
        assertThat(notifications).extracting(Notification::getId).containsExactly(notification.getId());
        assertThat(notifications).extracting(Notification::getUser).containsExactly(notification.getUser());
        assertThat(notifications).extracting(Notification::getCategory).containsExactly(notification.getCategory());
        assertThat(notifications).extracting(Notification::getMessage).containsExactly(notification.getMessage());
        assertThat(notifications).extracting(Notification::getStatus).containsExactly(notification.getStatus());
    }

    @Test
    @DisplayName("존재하는 회원에 대하 생성된 읽지 않은 알림에 대해 조회된다.")
    public void findUnReadNotificationsTest() {
        // given
        User user = createUser("test1@test.com");
        Notification notification = createNotification(user, Status.UNREAD);

        // when
        List<Notification> notifications = notificationQueryRepository.findUnReadNotifications(user.getEmail());

        // then
        assertThat(notifications).hasSize(1);
        assertThat(notifications).extracting(Notification::getId).containsExactly(notification.getId());
        assertThat(notifications).extracting(Notification::getUser).containsExactly(notification.getUser());
        assertThat(notifications).extracting(Notification::getCategory).containsExactly(notification.getCategory());
        assertThat(notifications).extracting(Notification::getMessage).containsExactly(notification.getMessage());
        assertThat(notifications).extracting(Notification::getStatus).containsExactly(Status.UNREAD);
    }

    @Test
    public void findReadNotificationsTest() {
        // given
        User user = createUser("test2@test.com");
        Notification notification = createNotification(user, Status.READ);

        // when
        List<Notification> notifications = notificationQueryRepository.findReadNotifications(user.getEmail());

        // then
        assertThat(notifications).hasSize(1);
        assertThat(notifications).extracting(Notification::getId).containsExactly(notification.getId());
        assertThat(notifications).extracting(Notification::getUser).containsExactly(notification.getUser());
        assertThat(notifications).extracting(Notification::getCategory).containsExactly(notification.getCategory());
        assertThat(notifications).extracting(Notification::getMessage).containsExactly(notification.getMessage());
        assertThat(notifications).extracting(Notification::getStatus).containsExactly(Status.READ);
    }

}
