package com.app.guttokback.group.application.service;

import com.app.guttokback.group.application.dto.serviceDto.SubscriptionGroupSaveInfo;
import com.app.guttokback.group.domain.entity.SubscriptionGroup;
import com.app.guttokback.group.domain.repository.SubscriptionGroupRepository;
import com.app.guttokback.user.application.service.UserService;
import com.app.guttokback.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionGroupService {

    private final SubscriptionGroupRepository subscriptionGroupRepository;
    private final UserService userService;
    private final GroupMemberService groupMemberService;

    @Transactional
    public void save(SubscriptionGroupSaveInfo subscriptionGroupSaveInfo) {
        User user = userService.findByUserEmail(subscriptionGroupSaveInfo.getEmail());

        SubscriptionGroup subscriptionGroup = SubscriptionGroup.builder()
                .user(user)
                .title(subscriptionGroupSaveInfo.getTitle())
                .subscription(subscriptionGroupSaveInfo.getSubscription())
                .paymentAmount(subscriptionGroupSaveInfo.getPaymentAmount())
                .paymentMethod(subscriptionGroupSaveInfo.getPaymentMethod())
                .paymentCycle(subscriptionGroupSaveInfo.getPaymentCycle())
                .paymentDay(subscriptionGroupSaveInfo.getPaymentDay())
                .notice(subscriptionGroupSaveInfo.getNotice())
                .build();

        subscriptionGroupRepository.save(subscriptionGroup);

        groupMemberService.addMember(user, subscriptionGroup);
    }

}
