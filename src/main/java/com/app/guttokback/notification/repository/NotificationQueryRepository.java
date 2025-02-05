package com.app.guttokback.notification.repository;

import com.app.guttokback.global.apiResponse.util.PageOption;
import com.app.guttokback.notification.domain.NotificationEntity;
import com.app.guttokback.notification.domain.Status;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.guttokback.notification.domain.QNotificationEntity.notificationEntity;
import static com.app.guttokback.user.domain.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<NotificationEntity> findPageNotifications(PageOption pageOption) {
        return jpaQueryFactory
                .selectFrom(notificationEntity)
                .innerJoin(notificationEntity.user, userEntity)
                .fetchJoin()
                .where(userEntity.email.eq(pageOption.getUserEmail())
                        .and(lastIdCondition(pageOption.getLastId())))
                .orderBy(notificationEntity.id.desc())
                .limit(pageOption.getSize() + 1)
                .fetch();
    }

    public List<NotificationEntity> findUnReadNotifications(String userEmail) {
        return jpaQueryFactory
                .selectFrom(notificationEntity)
                .innerJoin(notificationEntity.user, userEntity)
                .fetchJoin()
                .where(notificationEntity.status.eq(Status.UNREAD)
                        .and(userEntity.email.eq(userEmail)))
                .fetch();
    }

    private BooleanExpression lastIdCondition(Long lastId) {
        return lastId != null ? notificationEntity.id.lt(lastId) : null;
    }
}
