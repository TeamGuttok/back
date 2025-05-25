package com.app.guttokback.email.application.service;

import com.app.guttokback.email.domain.entity.EmailLog;
import com.app.guttokback.email.domain.enums.EmailType;
import com.app.guttokback.email.domain.repository.EmailLogRepository;
import com.app.guttokback.user.application.service.UserService;
import com.app.guttokback.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailLogService {

    private final EmailLogRepository emailLogRepository;
    private final UserService userService;

    @Transactional
    public void save(String userEmail, EmailType emailType) {
        User user = userService.findByUserEmail(userEmail);
        EmailLog emailLog = emailLogRepository.findByUserAndEmailType(user, emailType);
        if (emailLog == null) {
            emailLogRepository.save(EmailLog.builder()
                    .user(user)
                    .emailType(emailType)
                    .count(1)
                    .build()
            );
        } else {
            emailLog.increaseCount();
        }
    }
}
