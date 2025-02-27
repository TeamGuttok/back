package com.app.guttokback.notification.domain.repository;

import com.app.guttokback.notification.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
