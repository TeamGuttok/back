package com.app.guttokback.global.aws.ses.service;

import com.app.guttokback.global.aws.ses.dto.EmailInfo;
import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public EmailInfo createRemainderTemplate(List<UserSubscriptionEntity> userSubscriptions, UserEntity user, long totalAmount) {
        // 사용자 정보 템플릿 데이터 설정
        Context context = new Context();
        context.setVariable("nickName", user.getNickName());
        context.setVariable("subscriptions", userSubscriptions);
        context.setVariable("totalAmount", totalAmount);

        // 템플릿을 렌더링하여 HTML 콘텐츠 생성
        String content = templateEngine.process("reminder-email", context);

        // 이메일 발송 객체 생성
        return EmailInfo.builder()
                .to(Collections.singletonList(user.getEmail()))
                .subject("구똑 - 납부 리마인드")
                .content(content)
                .build();
    }

    public EmailInfo createCertificationNumberTemplate(String CertificationNumber, String email) {

        Context context = new Context();
        context.setVariable("authCode", CertificationNumber);

        String content = templateEngine.process("CertificationNumber-email", context);

        return EmailInfo.builder()
                .to(Collections.singletonList(email))
                .subject("구똑 - 인증코드")
                .content(content)
                .build();
    }
}
