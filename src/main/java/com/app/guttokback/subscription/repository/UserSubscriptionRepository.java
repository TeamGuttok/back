package com.app.guttokback.subscription.repository;

import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, Long> {
}
