package com.app.guttokback.userSubscription.application.service;

import com.app.guttokback.common.api.PageOption;
import com.app.guttokback.common.api.PageResponse;
import com.app.guttokback.common.exception.CustomApplicationException;
import com.app.guttokback.common.exception.ErrorCode;
import com.app.guttokback.email.application.service.ReminderService;
import com.app.guttokback.user.application.service.UserService;
import com.app.guttokback.user.domain.entity.User;
import com.app.guttokback.userSubscription.application.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.userSubscription.application.dto.serviceDto.SubscriptionListInfo;
import com.app.guttokback.userSubscription.application.dto.serviceDto.SubscriptionSearchInfo;
import com.app.guttokback.userSubscription.application.dto.serviceDto.UserSubscriptionSaveInfo;
import com.app.guttokback.userSubscription.application.dto.serviceDto.UserSubscriptionUpdateInfo;
import com.app.guttokback.userSubscription.domain.entity.UserSubscription;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionQueryRepository;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionRepository;
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
        User user = userService.findByUserEmail(userSubscriptionSaveInfo.getEmail());

        validateSubscriptionDetails(userSubscriptionSaveInfo);

        UserSubscription userSubscription = UserSubscription.builder()
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
        reminderService.initializeReminder(userSubscription);

        userSubscriptionRepository.save(userSubscription);
    }

    public PageResponse<UserSubscriptionListResponse> list(PageOption pageOption) {
        userService.findByUserEmail(pageOption.getUserEmail());

        List<UserSubscription> userSubscriptions = userSubscriptionQueryRepository
                .findPagedUserSubscriptions(pageOption);

        boolean hasNext = userSubscriptions.size() > pageOption.getSize();

        List<UserSubscription> pagedSubscriptions = userSubscriptions.stream()
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
        UserSubscription userSubscription = findUserSubscriptionById(userSubscriptionId);
        User user = userService.findByUserEmail(userSubscriptionUpdateInfo.getEmail());

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
        UserSubscription userSubscription = findUserSubscriptionById(userSubscriptionId);
        User user = userService.findByUserEmail(email);

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

    private UserSubscription findUserSubscriptionById(Long id) {
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
