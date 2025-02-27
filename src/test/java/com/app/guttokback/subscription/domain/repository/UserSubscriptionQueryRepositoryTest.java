package com.app.guttokback.subscription.domain.repository;

import com.app.guttokback.common.api.PageOption;
import com.app.guttokback.common.infrastructure.config.QueryDslConfig;
import com.app.guttokback.user.domain.model.User;
import com.app.guttokback.user.domain.repository.UserRepository;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.PaymentMethod;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import com.app.guttokback.userSubscription.domain.model.UserSubscription;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionQueryRepository;
import com.app.guttokback.userSubscription.domain.repository.UserSubscriptionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({UserSubscriptionQueryRepository.class, QueryDslConfig.class})
class UserSubscriptionQueryRepositoryTest {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSubscriptionQueryRepository userSubscriptionQueryRepository;

    @AfterEach
    public void clear() {
        userSubscriptionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    private User createUser() {
        User user = User.builder()
                .email("test@test.com")
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        return userRepository.save(user);
    }

    @Test
    @DisplayName("존재하는 회원이 생성한 구독항목에 대하여 조회된다.")
    public void userSubscriptionListPageTest() {
        // given
        User savedUser = createUser();

        UserSubscription userSubscription = UserSubscription.builder()
                .user(savedUser)
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();
        UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);

        PageOption pageOption = new PageOption(
                null, null, 5
        );

        // when
        List<UserSubscription> list
                = userSubscriptionQueryRepository.findPagedUserSubscriptions(pageOption);

        // then
        assertThat(list).hasSize(1);
        assertThat(list).extracting(UserSubscription::getTitle)
                .containsExactly(savedUserSubscription.getTitle());
        assertThat(list).extracting(UserSubscription::getSubscription)
                .containsExactly(savedUserSubscription.getSubscription());
        assertThat(list).extracting(UserSubscription::getPaymentAmount)
                .containsExactly(savedUserSubscription.getPaymentAmount());
        assertThat(list).extracting(UserSubscription::getPaymentMethod)
                .containsExactly(savedUserSubscription.getPaymentMethod());
        assertThat(list).extracting(UserSubscription::getPaymentCycle)
                .containsExactly(savedUserSubscription.getPaymentCycle());
        assertThat(list).extracting(UserSubscription::getMemo)
                .containsExactly(savedUserSubscription.getMemo());
    }

}
