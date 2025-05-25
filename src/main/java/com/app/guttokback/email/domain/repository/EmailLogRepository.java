package com.app.guttokback.email.domain.repository;

import com.app.guttokback.email.domain.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
