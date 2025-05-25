package com.app.guttokback.userSubscription.domain.repository;

import com.app.guttokback.common.api.PageOption;
import com.app.guttokback.userSubscription.domain.entity.UserSubscription;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.app.guttokback.user.domain.entity.QUser.user;
import static com.app.guttokback.userSubscription.domain.entity.QUserSubscription.userSubscription;

@Repository
@RequiredArgsConstructor
public class UserSubscriptionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // UserSubscription 리스트 페이징 조회 쿼리
    public List<UserSubscription> findPagedUserSubscriptions(PageOption pageOption) {
        return jpaQueryFactory
                .selectFrom(userSubscription)
                .where(
                        user.email.eq(pageOption.getUserEmail()),
                        lastIdCondition(pageOption.getLastId())
                )
                .orderBy(userSubscription.id.desc())
                .limit(pageOption.getSize() + 1)
                .fetch();
    }

    // 알림여부 승인한 유저와 구독항목 조회 쿼리
    public List<UserSubscription> findActiveUserSubscriptions(LocalDate now) {
        return jpaQueryFactory
                .selectFrom(userSubscription)
                .innerJoin(userSubscription.user, user)
                .fetchJoin()
                .where(user.alarm.eq(true)
                        .and(userSubscription.reminderDate.eq(now.plusDays(1))))
                .fetch();
    }

    // 테스트 유저의 구독 항목 중 최신 3개 제외한 나머지를 조회
    public List<UserSubscription> findSubscriptionsToDeleteForTestUser(Long userId) {
        return jpaQueryFactory
                .selectFrom(userSubscription)
                .where(userSubscription.user.id.eq(userId))
                .orderBy(userSubscription.registerDate.desc())
                .offset(3)
                .fetch();
    }

    private BooleanExpression lastIdCondition(Long lastId) {
        return lastId != null ? userSubscription.id.lt(lastId) : null;
    }
}
