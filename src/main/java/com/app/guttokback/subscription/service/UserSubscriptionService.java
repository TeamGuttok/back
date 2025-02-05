package com.app.guttokback.subscription.service;

import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.apiResponse.util.PageOption;
import com.app.guttokback.global.aws.ses.service.ReminderService;
import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.subscription.dto.serviceDto.SubscriptionListInfo;
import com.app.guttokback.subscription.dto.serviceDto.SubscriptionSearchInfo;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionSaveInfo;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionUpdateInfo;
import com.app.guttokback.subscription.repository.UserSubscriptionQueryRepository;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserService userService;
    private final UserSubscriptionQueryRepository userSubscriptionQueryRepository;
    private final ReminderService reminderService;

    @Transactional
    public void save(UserSubscriptionSaveInfo userSubscriptionSaveInfo) {
        UserEntity user = userService.findByUserEmail(userSubscriptionSaveInfo.getEmail());

        validateSubscriptionDetails(userSubscriptionSaveInfo);

        UserSubscriptionEntity userSubscriptionEntity = UserSubscriptionEntity.builder()
                .user(user)
                .title(userSubscriptionSaveInfo.getTitle())
                .subscription(userSubscriptionSaveInfo.getSubscription())
                .paymentAmount(userSubscriptionSaveInfo.getPaymentAmount())
                .paymentMethod(userSubscriptionSaveInfo.getPaymentMethod())
                .paymentCycle(userSubscriptionSaveInfo.getPaymentCycle())
                .paymentDay(userSubscriptionSaveInfo.getPaymentDay())
                .memo(userSubscriptionSaveInfo.getMemo())
                .build();

        // 알림 여부 true 시 결제 주기에 따라 reminderDate 업데이트
        reminderService.initializeReminder(userSubscriptionEntity);

        userSubscriptionRepository.save(userSubscriptionEntity);
    }

    public PageResponse<UserSubscriptionListResponse> list(PageOption pageOption) {
        userService.findByUserEmail(pageOption.getUserEmail());

        List<UserSubscriptionEntity> userSubscriptions = userSubscriptionQueryRepository
                .findPagedUserSubscriptions(pageOption);

        boolean hasNext = userSubscriptions.size() > pageOption.getSize();

        List<UserSubscriptionEntity> pagedSubscriptions = userSubscriptions.stream()
                .limit(pageOption.getSize())
                .toList();

        return PageResponse
                .of(pagedSubscriptions.stream()
                                .map(UserSubscriptionListResponse::of)
                                .toList(),
                        pageOption.getSize(),
                        hasNext
                );
    }

    @Transactional
    public void update(Long userSubscriptionId, UserSubscriptionUpdateInfo userSubscriptionUpdateInfo) {
        UserSubscriptionEntity userSubscription = findUserSubscriptionById(userSubscriptionId);
        UserEntity user = userService.findByUserEmail(userSubscriptionUpdateInfo.getEmail());

        validateUserSubscriptionOwnership(userSubscription.getUser().getId(), user.getId());

        int previousPaymentDay = userSubscription.getPaymentDay();
        PaymentCycle previousPaymentCycle = userSubscription.getPaymentCycle();

        userSubscription.update(
                userSubscriptionUpdateInfo.getTitle(),
                userSubscriptionUpdateInfo.getPaymentAmount(),
                userSubscriptionUpdateInfo.getPaymentMethod(),
                userSubscriptionUpdateInfo.getPaymentStatus(),
                userSubscriptionUpdateInfo.getPaymentCycle(),
                userSubscriptionUpdateInfo.getPaymentDay(),
                userSubscriptionUpdateInfo.getMemo());

        // PaymentDay, PaymentCycle 변경 시 리마인더 발송일 변경
        reminderService.updateReminderIfPaymentDetailsChanged(userSubscription, previousPaymentDay, previousPaymentCycle);
    }

    @Transactional
    public void delete(Long userSubscriptionId, String email) {
        UserSubscriptionEntity userSubscription = findUserSubscriptionById(userSubscriptionId);
        UserEntity user = userService.findByUserEmail(email);

        validateUserSubscriptionOwnership(userSubscription.getUser().getId(), user.getId());

        userSubscriptionRepository.delete(userSubscription);
    }

    public List<SubscriptionListInfo> subscriptionList(SubscriptionSearchInfo subscriptionSearchInfo) {
        return Arrays.stream(Subscription.values())
                .filter(subscription ->
                        (subscriptionSearchInfo.getName() == null ||
                                subscription.name().toLowerCase().contains(subscriptionSearchInfo.getName().toLowerCase()) ||
                                subscription.getName().toLowerCase().contains(subscriptionSearchInfo.getName().toLowerCase()))
                )
                .map(subscription -> new SubscriptionListInfo(subscription.name(), subscription.getName()))
                .toList();
    }

    private UserSubscriptionEntity findUserSubscriptionById(Long id) {
        return userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new CustomApplicationException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND));
    }

    private void validateSubscriptionDetails(UserSubscriptionSaveInfo userSubscriptionSaveInfo) {
        if (userSubscriptionSaveInfo.getSubscription() == Subscription.CUSTOM_INPUT) {
            // 구독 서비스가 CUSTOM_INPUT일 때 제목이 없으면 예외 처리
            if (userSubscriptionSaveInfo.getTitle() == null || userSubscriptionSaveInfo.getTitle().isEmpty()) {
                throw new CustomApplicationException(ErrorCode.MISSING_TITLE);
            }
        } else {
            // 구독 서비스가 CUSTOM_INPUT이 아닐 때 제목이 존재하면 예외 처리
            if (userSubscriptionSaveInfo.getTitle() != null && !userSubscriptionSaveInfo.getTitle().isEmpty()) {
                throw new CustomApplicationException(ErrorCode.INVALID_INPUT_TITLE);
            }
        }
    }

    private void validateUserSubscriptionOwnership(Long userSubscriptionUserId, Long userId) {
        if (!userSubscriptionUserId.equals(userId)) {
            throw new CustomApplicationException(ErrorCode.PERMISSION_DENIED);
        }
    }
}
