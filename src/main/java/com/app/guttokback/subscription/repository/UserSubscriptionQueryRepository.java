package com.app.guttokback.subscription.repository;

import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionListInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.guttokback.subscription.domain.QUserSubscriptionEntity.userSubscriptionEntity;

@Repository
@RequiredArgsConstructor
public class UserSubscriptionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<UserSubscriptionEntity> findPagedUserSubscriptions(UserSubscriptionListInfo userSubscriptionListInfo) {
        return jpaQueryFactory
                .selectFrom(userSubscriptionEntity)
                .where(lastIdCondition(userSubscriptionListInfo.getLastId()))
                .orderBy(userSubscriptionEntity.id.desc())
                .limit(userSubscriptionListInfo.getSize() + 1)
                .fetch();
    }

    private BooleanExpression lastIdCondition(Long lastId) {
        return lastId != null ? userSubscriptionEntity.id.lt(lastId) : null;
    }
}
