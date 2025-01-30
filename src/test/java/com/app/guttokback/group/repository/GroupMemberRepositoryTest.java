package com.app.guttokback.group.repository;

import com.app.guttokback.group.domain.GroupMemberEntity;
import com.app.guttokback.group.domain.SubscriptionGroupEntity;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
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

    private UserEntity createUser(String email) {
        UserEntity user = UserEntity.builder()
                .email(email)
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        return userRepository.save(user);
    }

    private SubscriptionGroupEntity createSubscriptionGroup(UserEntity user) {
        SubscriptionGroupEntity subscriptionGroupEntity = SubscriptionGroupEntity.builder()
                .user(user)
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .notice("test")
                .build();
        return subscriptionGroupRepository.save(subscriptionGroupEntity);
    }

    @Test
    @DisplayName("group_members 테이블에 매핑된 그룹 멤버가 존재하면 true를 반환한다.")
    public void existsByUserAndSubscriptionGroupIsTrueTest() {
        // given
        UserEntity user = createUser("test@test.com");
        SubscriptionGroupEntity subscriptionGroup = createSubscriptionGroup(user);

        GroupMemberEntity groupMemberEntity = GroupMemberEntity.builder()
                .user(user)
                .subscriptionGroup(subscriptionGroup)
                .build();
        groupMemberRepository.save(groupMemberEntity);

        // when
        boolean exists = groupMemberRepository.existsByUserAndSubscriptionGroup(user, subscriptionGroup);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("group_members 테이블에 매핑된 그룹 멤버가 존재하지않으면 false를 반환한다.")
    public void existsByUserAndSubscriptionGroupIsFalseTest() {
        // given
        UserEntity user = createUser("test@test.com");
        SubscriptionGroupEntity subscriptionGroup = createSubscriptionGroup(user);

        // when
        boolean exists = groupMemberRepository.existsByUserAndSubscriptionGroup(user, subscriptionGroup);

        // then
        assertThat(exists).isFalse();
    }

}
