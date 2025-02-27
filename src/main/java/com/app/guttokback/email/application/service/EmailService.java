package com.app.guttokback.email.application.service;

import com.app.guttokback.email.application.dto.serviceDto.EmailInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${aws.ses.send-mail-from}")
    private String sender;
    private final SesClient sesClient;

    public void sendEmail(EmailInfo emailInfo) {
        try {
            sesClient.sendEmail(emailInfo.toSendEmailRequest(sender));
        } catch (Exception exception) {
            log.error("이메일 전송 실패", exception);
        }
    }
}
