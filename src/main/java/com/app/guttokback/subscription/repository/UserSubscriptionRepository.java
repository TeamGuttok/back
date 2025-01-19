package com.app.guttokback.subscription.repository;

import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, Long> {

    List<UserSubscriptionEntity> findAllByUserId(Long userId);
}
