package com.app.guttokback.userSubscription.domain.repository;

import com.app.guttokback.userSubscription.domain.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    List<UserSubscription> findAllByUserId(Long userId);
}
