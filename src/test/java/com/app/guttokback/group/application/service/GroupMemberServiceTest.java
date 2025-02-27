package com.app.guttokback.group.application.service;

import com.app.guttokback.common.exception.CustomApplicationException;
import com.app.guttokback.group.domain.model.GroupMember;
import com.app.guttokback.group.domain.model.SubscriptionGroup;
import com.app.guttokback.group.domain.repository.GroupMemberRepository;
import com.app.guttokback.group.domain.repository.SubscriptionGroupRepository;
import com.app.guttokback.user.domain.model.User;
import com.app.guttokback.user.domain.repository.UserRepository;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.PaymentMethod;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GroupMemberServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionGroupRepository subscriptionGroupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private GroupMemberService groupMemberService;

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
    @Transactional
    @DisplayName("그룹과 유저정보가 매핑되어 정상적으로 저장된다.")
    public void addMemberTest() {
        // given
        User user = createUser("test@test.com");
        SubscriptionGroup subscriptionGroup = createSubscriptionGroup(user);

        // when
        groupMemberService.addMember(user, subscriptionGroup);

        // then
        GroupMember groupMember = groupMemberRepository.findAll().getFirst();
        assertThat(groupMember.getUser()).isEqualTo(user);
        assertThat(groupMember.getSubscriptionGroup()).isEqualTo(subscriptionGroup);
    }

    @Test
    @DisplayName("이미 참가한 그룹에 참가 시도 시 예외가 발생한다.")
    public void addMemberByValidateJoinGroupTest() {
        // given
        User user = createUser("test@test.com");
        SubscriptionGroup subscriptionGroup = createSubscriptionGroup(user);
        groupMemberService.addMember(user, subscriptionGroup);

        // when
        CustomApplicationException exception = assertThrows(CustomApplicationException.class,
                () -> groupMemberService.addMember(user, subscriptionGroup));

        // then
        assertThat(exception)
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage("이미 참가한 그룹입니다.");
    }

}
