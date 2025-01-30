package com.app.guttokback.group.service;

import com.app.guttokback.group.domain.SubscriptionGroupEntity;
import com.app.guttokback.group.dto.serviceDto.SubscriptionGroupSaveInfo;
import com.app.guttokback.group.repository.SubscriptionGroupRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.service.UserService;
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
        UserEntity user = userService.findByUserEmail(subscriptionGroupSaveInfo.getEmail());

        SubscriptionGroupEntity subscriptionGroupEntity = SubscriptionGroupEntity.builder()
                .user(user)
                .title(subscriptionGroupSaveInfo.getTitle())
                .subscription(subscriptionGroupSaveInfo.getSubscription())
                .paymentAmount(subscriptionGroupSaveInfo.getPaymentAmount())
                .paymentMethod(subscriptionGroupSaveInfo.getPaymentMethod())
                .paymentCycle(subscriptionGroupSaveInfo.getPaymentCycle())
                .paymentDay(subscriptionGroupSaveInfo.getPaymentDay())
                .notice(subscriptionGroupSaveInfo.getNotice())
                .build();

        subscriptionGroupRepository.save(subscriptionGroupEntity);

        groupMemberService.addMember(user, subscriptionGroupEntity);
    }

}
