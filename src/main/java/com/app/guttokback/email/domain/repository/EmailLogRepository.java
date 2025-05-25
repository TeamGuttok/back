package com.app.guttokback.email.domain.repository;

import com.app.guttokback.email.domain.entity.EmailLog;
import com.app.guttokback.email.domain.enums.EmailType;
import com.app.guttokback.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    EmailLog findByUserAndEmailType(User user, EmailType emailType);
}
