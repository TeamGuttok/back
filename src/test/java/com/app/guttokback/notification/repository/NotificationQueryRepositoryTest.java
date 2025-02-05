package com.app.guttokback.notification.repository;

import com.app.guttokback.global.apiResponse.util.PageOption;
import com.app.guttokback.global.queryDsl.QueryDslConfig;
import com.app.guttokback.notification.domain.Category;
import com.app.guttokback.notification.domain.NotificationEntity;
import com.app.guttokback.notification.domain.Status;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
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

    private UserEntity createUser(String email) {
        UserEntity user = UserEntity.builder()
                .email(email)
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        return userRepository.save(user);
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
    @DisplayName("존재하는 회원에 대해 생성된 알림에 대해 조회된다.")
    public void findPageNotificationsTest() {
        // given
        UserEntity user = createUser("test@test.com");
        NotificationEntity notification = createNotification(user);

        PageOption pageOption = new PageOption(user.getEmail(), null, 5);

        // when
        List<NotificationEntity> notifications = notificationQueryRepository.findPageNotifications(pageOption);

        // then
        assertThat(notifications).hasSize(1);
        assertThat(notifications).extracting(NotificationEntity::getId).containsExactly(notification.getId());
        assertThat(notifications).extracting(NotificationEntity::getUser).containsExactly(notification.getUser());
        assertThat(notifications).extracting(NotificationEntity::getCategory).containsExactly(notification.getCategory());
        assertThat(notifications).extracting(NotificationEntity::getMessage).containsExactly(notification.getMessage());
        assertThat(notifications).extracting(NotificationEntity::getStatus).containsExactly(notification.getStatus());
    }

    @Test
    @DisplayName("존재하는 회원에 대하 생성된 읽지 않은 알림에 대해 조회된다.")
    public void findUnReadNotificationsTest() {
        // given
        UserEntity user = createUser("test1@test.com");
        NotificationEntity notification = createNotification(user);

        // when
        List<NotificationEntity> notifications = notificationQueryRepository.findUnReadNotifications(user.getEmail());

        // then
        assertThat(notifications).hasSize(1);
        assertThat(notifications).extracting(NotificationEntity::getId).containsExactly(notification.getId());
        assertThat(notifications).extracting(NotificationEntity::getUser).containsExactly(notification.getUser());
        assertThat(notifications).extracting(NotificationEntity::getCategory).containsExactly(notification.getCategory());
        assertThat(notifications).extracting(NotificationEntity::getMessage).containsExactly(notification.getMessage());
        assertThat(notifications).extracting(NotificationEntity::getStatus).containsExactly(Status.UNREAD);
    }

}
