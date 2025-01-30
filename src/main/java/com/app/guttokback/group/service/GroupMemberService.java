package com.app.guttokback.group.service;

import com.app.guttokback.group.domain.GroupMemberEntity;
import com.app.guttokback.group.domain.SubscriptionGroupEntity;
import com.app.guttokback.group.repository.GroupMemberRepository;
import com.app.guttokback.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public void addMember(UserEntity user, SubscriptionGroupEntity subscriptionGroup) {
        GroupMemberEntity groupMemberEntity = GroupMemberEntity.builder()
                .user(user)
                .subscriptionGroup(subscriptionGroup)
                .build();

        groupMemberRepository.save(groupMemberEntity);
    }
}
