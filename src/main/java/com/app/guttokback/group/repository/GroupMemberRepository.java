package com.app.guttokback.group.repository;

import com.app.guttokback.group.domain.GroupMemberEntity;
import com.app.guttokback.group.domain.SubscriptionGroupEntity;
import com.app.guttokback.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {

    boolean existsByUserAndSubscriptionGroup(UserEntity user, SubscriptionGroupEntity subscriptionGroup);

}
