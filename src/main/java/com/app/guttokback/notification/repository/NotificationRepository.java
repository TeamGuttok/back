package com.app.guttokback.notification.repository;

import com.app.guttokback.notification.domain.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    
}
