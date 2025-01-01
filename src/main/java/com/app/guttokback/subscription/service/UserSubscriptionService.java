package com.app.guttokback.subscription.service;

import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import com.app.guttokback.subscription.domain.SubscriptionEntity;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionListInfo;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionUpdateInfo;
import com.app.guttokback.subscription.repository.UserSubscriptionQueryRepository;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final UserSubscriptionQueryRepository userSubscriptionQueryRepository;

    @Transactional
    public void save(UserSubscriptionSaveInfo userSubscriptionSaveInfo) {
        UserEntity user = userService.userFindById(userSubscriptionSaveInfo.getUserId());
        SubscriptionEntity subscription = subscriptionService.findSubscriptionById(userSubscriptionSaveInfo.getSubscriptionId());

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

    public PageResponse<UserSubscriptionListResponse> list(UserSubscriptionListInfo userSubscriptionListInfo) {
        userService.userFindById(userSubscriptionListInfo.getUserId());

        List<UserSubscriptionEntity> userSubscriptions = userSubscriptionQueryRepository
                .findPagedUserSubscriptions(userSubscriptionListInfo);

        boolean hasNext = userSubscriptions.size() > userSubscriptionListInfo.getSize();

        List<UserSubscriptionEntity> pagedSubscriptions = userSubscriptions.stream()
                .limit(userSubscriptionListInfo.getSize())
                .toList();

        return PageResponse
                .of(pagedSubscriptions.stream()
                                .map(UserSubscriptionListResponse::of)
                                .toList(),
                        userSubscriptionListInfo.getSize(),
                        hasNext
                );
    }

    @Transactional
    public void update(Long id, UserSubscriptionUpdateInfo userSubscriptionUpdateInfo) {
        UserSubscriptionEntity userSubscription = findUserSubscriptionById(id);
        System.out.println("!!!" + userSubscription.getTitle());
        userSubscription.update(
                userSubscriptionUpdateInfo.getTitle(),
                userSubscriptionUpdateInfo.getPaymentAmount(),
                userSubscriptionUpdateInfo.getPaymentMethod(),
                userSubscriptionUpdateInfo.getStartDate(),
                userSubscriptionUpdateInfo.getPaymentCycle(),
                userSubscriptionUpdateInfo.getPaymentDay(),
                userSubscriptionUpdateInfo.getMemo());
    }

    private UserSubscriptionEntity findUserSubscriptionById(Long id) {
        return userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new CustomApplicationException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND));
    }
}
