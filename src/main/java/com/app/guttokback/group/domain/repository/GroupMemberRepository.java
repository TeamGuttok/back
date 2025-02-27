package com.app.guttokback.group.domain.repository;

import com.app.guttokback.group.domain.entity.GroupMember;
import com.app.guttokback.group.domain.entity.SubscriptionGroup;
import com.app.guttokback.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByUserAndSubscriptionGroup(User user, SubscriptionGroup subscriptionGroup);

}
