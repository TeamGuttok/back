package com.app.guttokback.group.application.service;

import com.app.guttokback.common.exception.CustomApplicationException;
import com.app.guttokback.common.exception.ErrorCode;
import com.app.guttokback.group.domain.model.GroupMember;
import com.app.guttokback.group.domain.model.SubscriptionGroup;
import com.app.guttokback.group.domain.repository.GroupMemberRepository;
import com.app.guttokback.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public void addMember(User user, SubscriptionGroup subscriptionGroup) {
        validateJoinGroup(user, subscriptionGroup);
        GroupMember groupMember = GroupMember.builder()
                .user(user)
                .subscriptionGroup(subscriptionGroup)
                .build();

        groupMemberRepository.save(groupMember);
    }

    private void validateJoinGroup(User user, SubscriptionGroup subscriptionGroup) {
        if (groupMemberRepository.existsByUserAndSubscriptionGroup(user, subscriptionGroup)) {
            throw new CustomApplicationException(ErrorCode.MEMBER_ALREADY_JOINED_GROUP);
        }
    }

}
