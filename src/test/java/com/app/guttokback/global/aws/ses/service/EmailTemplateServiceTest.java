package com.app.guttokback.global.aws.ses.service;

import com.app.guttokback.global.aws.ses.dto.EmailInfo;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.repository.UserSubscriptionRepository;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmailTemplateServiceTest {

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @BeforeEach
    public void setUp() {
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        emailTemplateService = new EmailTemplateService(templateEngine);
    }

    @AfterEach
    public void clear() {
        userSubscriptionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
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

    private UserSubscriptionEntity createUserSubscription(UserEntity user) {
        UserSubscriptionEntity userSubscription = new UserSubscriptionEntity(
                user,
                "test",
                Subscription.CUSTOM_INPUT,
                10000,
                PaymentMethod.CARD,
                PaymentCycle.MONTHLY,
                1,
                "test");
        return userSubscriptionRepository.save(userSubscription);
    }

    @Test
    @DisplayName("./resources/templates 경로에 지정해놓은 HTML 템플릿을 정상적으로 생성한다.")
    public void test() throws Exception {
        // given
        UserEntity user = createUser();

        UserSubscriptionEntity userSubscription = createUserSubscription(user);
        List<UserSubscriptionEntity> userSubscriptions = List.of(userSubscription);

        long totalAmount = 100;

        templateEngine.getTemplateResolvers()
                .forEach(resolver -> ((StringTemplateResolver) resolver).setTemplateMode("HTML"));
        ((StringTemplateResolver) templateEngine.getTemplateResolvers().iterator().next())
                .setResolvablePatterns(Collections.singleton("reminder-email"));
        ((StringTemplateResolver) templateEngine.getTemplateResolvers().iterator().next())
                .setCacheable(false);

        // when
        EmailInfo emailInfo = emailTemplateService.createReminderTemplate(userSubscriptions, user, totalAmount);

        // then
        assertThat(emailInfo.getTo()).isEqualTo(List.of(user.getEmail()));
        assertThat(emailInfo.getSubject()).isEqualTo("구똑 - 납부 리마인드");
        assertThat(emailInfo.getContent()).isNotEmpty();
    }

}
