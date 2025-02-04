package com.app.guttokback.notification.service;

import com.app.guttokback.notification.domain.Category;
import com.app.guttokback.notification.domain.Message;
import com.app.guttokback.notification.domain.NotificationEntity;
import com.app.guttokback.notification.domain.Status;
import com.app.guttokback.notification.repository.NotificationRepository;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void userSaveNotification(UserEntity userEntity) {
        String message = String.format(Message.USER_SIGNUP_WELCOME.getMessage(), userEntity.getNickName());

        notificationRepository.save(NotificationEntity.builder()
                .user(userEntity)
                .category(Category.APPLICATION)
                .message(message)
                .status(Status.UNREAD)
                .build());
    }

    @Transactional
    public void reminderNotification(UserEntity userEntity, UserSubscriptionEntity userSubscriptionEntity) {
        String subscription = userSubscriptionEntity.getSubscription() == Subscription.CUSTOM_INPUT
                ? userSubscriptionEntity.getTitle() : userSubscriptionEntity.getSubscription().getName();

        String message = String.format(
                Message.USER_PAYMENT_REMINDER.getMessage(), userEntity.getNickName(), subscription
        );

        notificationRepository.save(NotificationEntity.builder()
                .user(userEntity)
                .category(Category.REMINDER)
                .message(message)
                .status(Status.UNREAD)
                .build());
    }
}
