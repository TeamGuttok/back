package com.app.guttokback.group.service;

import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.group.domain.SubscriptionGroupEntity;
import com.app.guttokback.group.dto.serviceDto.SubscriptionGroupSaveInfo;
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
class SubscriptionGroupServiceTest {

    @Autowired
    private SubscriptionGroupService subscriptionGroupService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionGroupRepository subscriptionGroupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;

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

    @Test
    @Transactional
    @DisplayName("존재하는 회원이 그룹 생성 시 해당회원을 생성자로하여 그룹이 정상적으로 생성된다.")
    public void savedSubscriptionGroupTest() {
        // given
        UserEntity user = createUser("test@test.com");

        SubscriptionGroupSaveInfo subscriptionGroupSaveInfo = SubscriptionGroupSaveInfo.builder()
                .email(user.getEmail())
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .notice("test")
                .build();

        // when
        subscriptionGroupService.save(subscriptionGroupSaveInfo);

        // then
        SubscriptionGroupEntity subscriptionGroup = subscriptionGroupRepository.findAll().getFirst();
        assertThat(subscriptionGroup.getUser().getEmail()).isEqualTo(subscriptionGroupSaveInfo.getEmail());
        assertThat(subscriptionGroup.getTitle()).isEqualTo(subscriptionGroupSaveInfo.getTitle());
        assertThat(subscriptionGroup.getSubscription()).isEqualTo(subscriptionGroupSaveInfo.getSubscription());
        assertThat(subscriptionGroup.getPaymentAmount()).isEqualTo(subscriptionGroupSaveInfo.getPaymentAmount());
        assertThat(subscriptionGroup.getPaymentMethod()).isEqualTo(subscriptionGroupSaveInfo.getPaymentMethod());
        assertThat(subscriptionGroup.getPaymentCycle()).isEqualTo(subscriptionGroupSaveInfo.getPaymentCycle());
        assertThat(subscriptionGroup.getNotice()).isEqualTo(subscriptionGroupSaveInfo.getNotice());
    }

    @Test
    @DisplayName("존재하지 않는 회원이 그룹 생성 시 예외가 발생한다.")
    public void savedSubscriptionGroupByValidateUserTest() {
        // given
        SubscriptionGroupSaveInfo subscriptionGroupSaveInfo = SubscriptionGroupSaveInfo.builder()
                .email(null)
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .notice("test")
                .build();

        // when
        CustomApplicationException exception = assertThrows(CustomApplicationException.class,
                () -> subscriptionGroupService.save(subscriptionGroupSaveInfo));

        // then
        assertThat(exception)
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage("계정을 찾을 수 없습니다");
    }
}
