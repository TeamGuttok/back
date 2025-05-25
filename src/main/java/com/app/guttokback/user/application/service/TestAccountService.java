package com.app.guttokback.user.application.service;

import com.app.guttokback.common.security.Roles;
import com.app.guttokback.notification.application.service.NotificationService;
import com.app.guttokback.user.domain.entity.User;
import com.app.guttokback.user.domain.repository.UserRepository;
import com.app.guttokback.userSubscription.domain.entity.UserSubscription;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionQueryRepository;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestAccountService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final UserSubscriptionQueryRepository userSubscriptionQueryRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    /**
     * 테스트 계정 구독항목 삭제
     */
    @Transactional
    public void resetTestAccounts() {
        List<User> testUsers = userRepository.findTestAccounts(Roles.ROLE_TEST);

        for (User user : testUsers) {
            List<UserSubscription> toDelete = userSubscriptionQueryRepository.findSubscriptionsToDeleteForTestUser(user.getId());

            if (!toDelete.isEmpty()) {
                userSubscriptionRepository.deleteAll(toDelete);
                log.info("[RESET] 테스트 유저({})의 구독항목 {}개 삭제", user.getEmail(), toDelete.size());
            }
        }
    }

    /**
     * 테스트 계정 리마인더 처리
     */
    @Transactional
    public void processTestAccountReminders(User user, List<UserSubscription> userSubscriptions) {
        userSubscriptions.forEach(subscription -> notificationService.reminderNotification(user, subscription));
        userSubscriptions.forEach(UserSubscription::updateReminderDate);
        log.info("[TEST_USER] 이메일 발송 제외 - {}", user.getEmail());
    }
}
