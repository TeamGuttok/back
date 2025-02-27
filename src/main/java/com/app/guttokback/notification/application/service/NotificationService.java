package com.app.guttokback.notification.application.service;

import com.app.guttokback.common.api.PageOption;
import com.app.guttokback.common.api.PageResponse;
import com.app.guttokback.notification.application.dto.NotificationListResponse;
import com.app.guttokback.notification.domain.entity.Notification;
import com.app.guttokback.notification.domain.enums.Category;
import com.app.guttokback.notification.domain.enums.Message;
import com.app.guttokback.notification.domain.enums.Status;
import com.app.guttokback.notification.domain.repository.NotificationQueryRepository;
import com.app.guttokback.notification.domain.repository.NotificationRepository;
import com.app.guttokback.user.domain.entity.User;
import com.app.guttokback.userSubscription.domain.entity.UserSubscription;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationQueryRepository notificationQueryRepository;

    @Transactional
    public void userSaveNotification(User user) {
        String message = String.format(Message.USER_SIGNUP_WELCOME.getMessage(), user.getNickName());

        notificationRepository.save(Notification.builder()
                .user(user)
                .category(Category.APPLICATION)
                .message(message)
                .status(Status.UNREAD)
                .build());
    }

    @Transactional
    public void reminderNotification(User user, UserSubscription userSubscription) {
        String subscription = userSubscription.getSubscription() == Subscription.CUSTOM_INPUT
                ? userSubscription.getTitle() : userSubscription.getSubscription().getName();

        String message = String.format(
                Message.USER_PAYMENT_REMINDER.getMessage(), user.getNickName(), subscription
        );

        notificationRepository.save(Notification.builder()
                .user(user)
                .category(Category.REMINDER)
                .message(message)
                .status(Status.UNREAD)
                .build());
    }

    public PageResponse<NotificationListResponse> list(PageOption pageOption) {
        List<Notification> notifications = notificationQueryRepository.findPageNotifications(pageOption);

        boolean hasNext = notifications.size() > pageOption.getSize();

        List<Notification> pagedNotifications = notifications.stream()
                .limit(pageOption.getSize())
                .toList();

        return PageResponse
                .of(pagedNotifications.stream()
                                .map(NotificationListResponse::of)
                                .toList(),
                        pageOption.getSize(),
                        hasNext
                );
    }

    @Transactional
    public void statusUpdate(String userEmail) {
        List<Notification> unReadNotifications = notificationQueryRepository.findUnReadNotifications(userEmail);
        unReadNotifications.forEach(notificationEntity -> notificationEntity.statusUpdate(Status.READ));
    }

    @Transactional
    public void delete(String userEmail) {
        List<Notification> readNotifications = notificationQueryRepository.findReadNotifications(userEmail);
        notificationRepository.deleteAll(readNotifications);
    }
}
