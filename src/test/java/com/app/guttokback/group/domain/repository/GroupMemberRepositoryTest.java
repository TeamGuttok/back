package com.app.guttokback.group.domain.repository;

import com.app.guttokback.group.domain.model.GroupMember;
import com.app.guttokback.group.domain.model.SubscriptionGroup;
import com.app.guttokback.user.domain.model.User;
import com.app.guttokback.user.domain.repository.UserRepository;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.PaymentMethod;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GroupMemberRepositoryTest {

    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionGroupRepository subscriptionGroupRepository;

    @AfterEach
    public void clear() {
        groupMemberRepository.deleteAllInBatch();
        subscriptionGroupRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    private User createUser(String email) {
        User user = User.builder()
                .email(email)
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        return userRepository.save(user);
    }

    private SubscriptionGroup createSubscriptionGroup(User user) {
        SubscriptionGroup subscriptionGroup = SubscriptionGroup.builder()
                .user(user)
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .notice("test")
                .build();
        return subscriptionGroupRepository.save(subscriptionGroup);
    }

    @Test
    @DisplayName("group_members 테이블에 매핑된 그룹 멤버가 존재하면 true를 반환한다.")
    public void existsByUserAndSubscriptionGroupIsTrueTest() {
        // given
        User user = createUser("test@test.com");
        SubscriptionGroup subscriptionGroup = createSubscriptionGroup(user);

        GroupMember groupMember = GroupMember.builder()
                .user(user)
                .subscriptionGroup(subscriptionGroup)
                .build();
        groupMemberRepository.save(groupMember);

        // when
        boolean exists = groupMemberRepository.existsByUserAndSubscriptionGroup(user, subscriptionGroup);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("group_members 테이블에 매핑된 그룹 멤버가 존재하지않으면 false를 반환한다.")
    public void existsByUserAndSubscriptionGroupIsFalseTest() {
        // given
        User user = createUser("test@test.com");
        SubscriptionGroup subscriptionGroup = createSubscriptionGroup(user);

        // when
        boolean exists = groupMemberRepository.existsByUserAndSubscriptionGroup(user, subscriptionGroup);

        // then
        assertThat(exists).isFalse();
    }

}
