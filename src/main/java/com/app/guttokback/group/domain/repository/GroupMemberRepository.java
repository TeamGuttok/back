package com.app.guttokback.group.domain.repository;

import com.app.guttokback.group.domain.model.GroupMember;
import com.app.guttokback.group.domain.model.SubscriptionGroup;
import com.app.guttokback.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByUserAndSubscriptionGroup(User user, SubscriptionGroup subscriptionGroup);

}
