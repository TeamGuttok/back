package com.app.guttokback.group.service;

import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.group.domain.GroupMemberEntity;
import com.app.guttokback.group.domain.SubscriptionGroupEntity;
import com.app.guttokback.group.repository.GroupMemberRepository;
import com.app.guttokback.group.repository.SubscriptionGroupRepository;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
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
    @Transactional
    @DisplayName("그룹과 유저정보가 매핑되어 정상적으로 저장된다.")
    public void addMemberTest() {
        // given
        UserEntity user = createUser("test@test.com");
        SubscriptionGroupEntity subscriptionGroup = createSubscriptionGroup(user);

        // when
        groupMemberService.addMember(user, subscriptionGroup);

        // then
        GroupMemberEntity groupMember = groupMemberRepository.findAll().getFirst();
        assertThat(groupMember.getUser()).isEqualTo(user);
        assertThat(groupMember.getSubscriptionGroup()).isEqualTo(subscriptionGroup);
    }

    @Test
    @DisplayName("이미 참가한 그룹에 참가 시도 시 예외가 발생한다.")
    public void addMemberByValidateJoinGroupTest() {
        // given
        UserEntity user = createUser("test@test.com");
        SubscriptionGroupEntity subscriptionGroup = createSubscriptionGroup(user);
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
