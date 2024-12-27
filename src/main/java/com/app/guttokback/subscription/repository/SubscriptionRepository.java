package com.app.guttokback.subscription.repository;

import com.app.guttokback.subscription.domain.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
}
