package com.app.guttokback.email.application.service;

import com.app.guttokback.notification.application.service.NotificationService;
import com.app.guttokback.userSubscription.domain.entity.UserSubscription;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionQueryRepository;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final UserSubscriptionQueryRepository userSubscriptionQueryRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final NotificationService notificationService;

    @Transactional
    public void sendReminder(LocalDate now) {
        userSubscriptionQueryRepository.findActiveUserSubscriptions(now)
                .stream()
                // 유저 별 그룹화
                .collect(Collectors.groupingBy(UserSubscription::getUser))
                .forEach((user, userSubscriptions) -> {
                    // 유저 별 총 금액 연산
                    long totalAmount = userSubscriptions.stream()
                            .mapToLong(UserSubscription::getPaymentAmount)
                            .sum();
                    // 템플릿에 필요한 데이터 전달
                    emailService.sendEmail(emailTemplateService.createReminderTemplate(userSubscriptions, user, totalAmount));
                    // 구독 항목 별 알림 저장
                    userSubscriptions.forEach(subscription -> notificationService.reminderNotification(user, subscription));
                    // 이메일 발송 된 구독항목 ReminderDate Payment Day, Cycle에 따른 변경
                    userSubscriptions.forEach(UserSubscription::updateReminderDate);
                });
    }

    // 유저 알림여부 변경 시 실행되는 메서드
    public void updateAllRemindersForUser(Long userId) {
        List<UserSubscription> userSubscriptions = userSubscriptionRepository.findAllByUserId(userId);
        userSubscriptions.forEach(UserSubscription::updateReminderDate);
        userSubscriptionRepository.saveAll(userSubscriptions);
    }

    // 구독 서비스의 Payment Day, Cycle 변경 시 실행되는 메서드
    public void updateReminderIfPaymentDetailsChanged(
            UserSubscription userSubscription,
            int previousPaymentDay,
            PaymentCycle previousPaymentCycle
    ) {
        boolean hasChanges = previousPaymentDay != userSubscription.getPaymentDay()
                || previousPaymentCycle != userSubscription.getPaymentCycle();

        if (hasChanges && userSubscription.getUser().isAlarm()) {
            userSubscription.updateReminderDate();
        }
    }

    // 새로운 구독 서비스 저장 시 실행되는 메서드
    public void initializeReminder(UserSubscription subscriptionEntity) {
        if (subscriptionEntity.getUser().isAlarm()) {
            subscriptionEntity.updateReminderDate();
        }
    }

}
