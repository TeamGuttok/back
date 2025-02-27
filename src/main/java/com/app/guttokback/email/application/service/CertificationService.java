package com.app.guttokback.email.application.service;

import com.app.guttokback.common.exception.CustomApplicationException;
import com.app.guttokback.common.exception.ErrorCode;
import com.app.guttokback.email.application.dto.serviceDto.EmailInfo;
import com.app.guttokback.email.application.dto.serviceDto.GetEmailInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final RedisTemplate<String, String> redisTemplate;
    private final EmailTemplateService emailTemplateService;
    private final EmailService emailService;

    private String createCertificationNumber() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        return random.ints(6, 0, chars.length())
                .mapToObj(chars::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private String saveCertificationNumber(String email) {
        // 인증 요청 횟수 제한 키
        String requestCountKey = "certification:count:" + email;
        // 인증 코드 저장 키
        String codeKey = "certification:" + email;

        // 현재 요청 횟수 가져오기
        Integer currentCount = redisTemplate.opsForValue().get(requestCountKey) != null
                ? Integer.parseInt(redisTemplate.opsForValue().get(requestCountKey))
                : 0;

        if (currentCount >= 5) {
            throw new CustomApplicationException(ErrorCode.OVER_REQUEST_COUNT);
        }

        String code = createCertificationNumber();

        redisTemplate.delete(codeKey);
        redisTemplate.opsForValue().set(codeKey, code, 10, TimeUnit.MINUTES);
        redisTemplate.opsForValue().increment(requestCountKey);
        redisTemplate.expire(requestCountKey, 24, TimeUnit.HOURS);

        return code;
    }

    public void sendCertificationNumber(GetEmailInfo getEmailInfo) {
        String code = saveCertificationNumber(getEmailInfo.getEmail());
        EmailInfo certificationNumberTemplate = emailTemplateService.createCertificationNumberTemplate(code, getEmailInfo.getEmail());
        emailService.sendEmail(certificationNumberTemplate);
    }

}
