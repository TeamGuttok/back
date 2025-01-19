package com.app.guttokback.global.aws.ses.service;

import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.repository.UserSubscriptionQueryRepository;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void sendReminder(LocalDate now) {
        userSubscriptionQueryRepository.findActiveUserSubscriptions(now)
                .stream()
                // 유저 별 그룹화
                .collect(Collectors.groupingBy(UserSubscriptionEntity::getUser))
                .forEach((user, userSubscriptions) -> {
                    // 유저 별 총 금액 연산
                    long totalAmount = userSubscriptions.stream()
                            .mapToLong(UserSubscriptionEntity::getPaymentAmount)
                            .sum();
                    // 템플릿에 필요한 데이터 전달
                    emailService.sendEmail(emailTemplateService.createReminderTemplate(userSubscriptions, user, totalAmount));
                    updateReminder(user.getId());
                });
    }

    public void updateReminder(Long userId) {
        List<UserSubscriptionEntity> userSubscriptions = userSubscriptionRepository.findAllByUserId(userId);

        userSubscriptions
                .forEach(subscription -> {
                    LocalDate reminderDate = calculateInitialReminderDate(subscription);
                    subscription.updateReminderDate(reminderDate);
                });

        userSubscriptionRepository.saveAll(userSubscriptions);
    }

    private LocalDate calculateInitialReminderDate(UserSubscriptionEntity userSubscriptionEntity) {
        int paymentDay = userSubscriptionEntity.getPaymentDay();
        LocalDate now = LocalDate.now();
        LocalDate reminderSendDateTime = now.withDayOfMonth(paymentDay);

        reminderSendDateTime = switch (userSubscriptionEntity.getPaymentCycle()) {
            case YEARLY -> reminderSendDateTime.plusYears(1);
            case MONTHLY -> reminderSendDateTime.plusMonths(1);
            case WEEKLY -> reminderSendDateTime.plusWeeks(1);
        };
        return reminderSendDateTime;
    }
}
