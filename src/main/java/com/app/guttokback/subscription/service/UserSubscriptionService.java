package com.app.guttokback.subscription.service;

import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import com.app.guttokback.subscription.domain.SubscriptionEntity;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import com.app.guttokback.subscription.repository.SubscriptionRepository;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    public void save(UserSubscriptionSaveInfo userSubscriptionSaveInfo) {
        UserEntity user = userService.userFindById(userSubscriptionSaveInfo.getUserId());
        SubscriptionEntity subscription = findSubscriptionById(userSubscriptionSaveInfo.getSubscriptionId());

        userSubscriptionRepository.save(
                UserSubscriptionEntity.builder()
                        .user(user)
                        .title(userSubscriptionSaveInfo.getTitle())
                        .subscription(subscription)
                        .paymentAmount(userSubscriptionSaveInfo.getPaymentAmount())
                        .paymentMethod(userSubscriptionSaveInfo.getPaymentMethod())
                        .startDate(userSubscriptionSaveInfo.getStartDate())
                        .paymentCycle(userSubscriptionSaveInfo.getPaymentCycle())
                        .paymentDay(userSubscriptionSaveInfo.getPaymentDay())
                        .memo(userSubscriptionSaveInfo.getMemo())
                        .build());
    }

    private SubscriptionEntity findSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new CustomApplicationException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }
}
