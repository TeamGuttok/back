package com.app.guttokback.subscription.repository;

import com.app.guttokback.subscription.domain.UserSubscriptionEntity;
import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionListInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.guttokback.subscription.domain.QUserSubscriptionEntity.userSubscriptionEntity;
import static com.app.guttokback.user.domain.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserSubscriptionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // UserSubscription 리스트 페이징 조회 쿼리
    public List<UserSubscriptionEntity> findPagedUserSubscriptions(UserSubscriptionListInfo userSubscriptionListInfo) {
        return jpaQueryFactory
                .selectFrom(userSubscriptionEntity)
                .where(lastIdCondition(userSubscriptionListInfo.getLastId()))
                .orderBy(userSubscriptionEntity.id.desc())
                .limit(userSubscriptionListInfo.getSize() + 1)
                .fetch();
    }

    // 알림여부 승인한 유저와 구독항목 조회 쿼리
    public List<UserSubscriptionEntity> findActiveUserSubscriptions() {
        return jpaQueryFactory
                .selectFrom(userSubscriptionEntity)
                .innerJoin(userSubscriptionEntity.user, userEntity)
                .fetchJoin()
                .where(userEntity.alarm.eq(true))
                .fetch();
    }

    private BooleanExpression lastIdCondition(Long lastId) {
        return lastId != null ? userSubscriptionEntity.id.lt(lastId) : null;
    }
}
