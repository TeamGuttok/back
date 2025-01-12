package com.app.guttokback.global.aws.ses.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.ses.model.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailInfo {

    private List<String> to;
    private String subject;
    private String content;

    @Builder
    public EmailInfo(List<String> to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    // SES 에서 제공하는 이메일 전송 요청
    public SendEmailRequest toSendEmailRequest(String sender) {
        Destination destination = Destination.builder()
                .toAddresses(this.to)
                .build();

        Message message = Message.builder()
                .subject(createContent(this.subject))
                .body(Body.builder().html(createContent(this.content)).build())
                .build();

        return SendEmailRequest.builder()
                .source(sender)
                .destination(destination)
                .message(message)
                .build();
    }

    // Email Subject, Body UTF-8 설정
    private Content createContent(String text) {
        return Content.builder()
                .charset("UTF-8")
                .data(text)
                .build();
    }
}
