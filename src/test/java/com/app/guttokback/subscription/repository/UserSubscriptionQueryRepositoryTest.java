package com.app.guttokback.subscription.repository;

import com.app.guttokback.global.queryDsl.QueryDslConfig;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.SubscriptionEntity;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionListInfo;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
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
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserSubscriptionQueryRepository userSubscriptionQueryRepository;

    @AfterEach
    public void clear() {
        userSubscriptionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        subscriptionRepository.deleteAllInBatch();
    }

    private UserEntity createUser() {
        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password("!a1234567890")
                .nickName("test")
                .alarm(false)
                .build();
        return userRepository.save(user);
    }

    private SubscriptionEntity createSubscription() {
        SubscriptionEntity subscription = new SubscriptionEntity("test");
        return subscriptionRepository.save(subscription);
    }

    @Test
    @DisplayName("존재하는 회원이 생성한 구독항목에 대하여 조회된다.")
    public void userSubscriptionListPageTest() {
        // given
        UserEntity savedUser = createUser();

        SubscriptionEntity savedSubscription = createSubscription();

        UserSubscriptionEntity userSubscription = UserSubscriptionEntity.builder()
                .user(savedUser)
                .title("test")
                .subscription(savedSubscription)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .startDate(LocalDate.parse("2024-12-27"))
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .memo("test")
                .build();
        UserSubscriptionEntity savedUserSubscription = userSubscriptionRepository.save(userSubscription);

        UserSubscriptionListInfo userSubscriptionListInfo = new UserSubscriptionListInfo(
                null, null, 5
        );

        // when
        List<UserSubscriptionEntity> list
                = userSubscriptionQueryRepository.findPagedUserSubscriptions(userSubscriptionListInfo);

        // then
        assertThat(list).hasSize(1);
        assertThat(list).extracting(UserSubscriptionEntity::getTitle)
                .containsExactly(savedUserSubscription.getTitle());
        assertThat(list).extracting(UserSubscriptionEntity::getSubscription)
                .containsExactly(savedUserSubscription.getSubscription());
        assertThat(list).extracting(UserSubscriptionEntity::getPaymentAmount)
                .containsExactly(savedUserSubscription.getPaymentAmount());
        assertThat(list).extracting(UserSubscriptionEntity::getPaymentMethod)
                .containsExactly(savedUserSubscription.getPaymentMethod());
        assertThat(list).extracting(UserSubscriptionEntity::getStartDate)
                .containsExactly(savedUserSubscription.getStartDate());
        assertThat(list).extracting(UserSubscriptionEntity::getPaymentCycle)
                .containsExactly(savedUserSubscription.getPaymentCycle());
        assertThat(list).extracting(UserSubscriptionEntity::getMemo)
                .containsExactly(savedUserSubscription.getMemo());
    }

}