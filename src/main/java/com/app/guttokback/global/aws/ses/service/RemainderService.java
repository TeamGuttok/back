package com.app.guttokback.global.aws.ses.service;

import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.repository.UserSubscriptionQueryRepository;
import com.app.guttokback.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemainderService {

    private final UserSubscriptionQueryRepository userSubscriptionQueryRepository;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    public void sendReminder() {
        userSubscriptionQueryRepository.findActiveUserSubscriptions()
                .stream()
                // 유저 별 그룹화
                .collect(Collectors.groupingBy(UserSubscriptionEntity::getUser))
                .entrySet()
                .stream()
                .map(entry -> {
                    // 유저 별 총 금액 연산
                    UserEntity user = entry.getKey();
                    long totalAmount = entry.getValue().stream()
                            .mapToLong(UserSubscriptionEntity::getPaymentAmount)
                            .sum();
                    // 템플릿에 필요한 데이터 전달
                    return emailTemplateService.createRemainderTemplate(entry.getValue(), user, totalAmount);
                })
                .toList()
                // 유저 별 이메일 전송
                .forEach(emailService::sendEmail);
    }
}
