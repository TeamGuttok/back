package com.app.guttokback.notification.domain.repository;

import com.app.guttokback.common.api.PageOption;
import com.app.guttokback.notification.domain.enums.Status;
import com.app.guttokback.notification.domain.model.Notification;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.guttokback.notification.domain.model.QNotification.notification;
import static com.app.guttokback.user.domain.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Notification> findPageNotifications(PageOption pageOption) {
        return jpaQueryFactory
                .selectFrom(notification)
                .innerJoin(notification.user, user)
                .fetchJoin()
                .where(user.email.eq(pageOption.getUserEmail())
                        .and(lastIdCondition(pageOption.getLastId())))
                .orderBy(notification.id.desc())
                .limit(pageOption.getSize() + 1)
                .fetch();
    }

    public List<Notification> findUnReadNotifications(String userEmail) {
        return jpaQueryFactory
                .selectFrom(notification)
                .innerJoin(notification.user, user)
                .fetchJoin()
                .where(notification.status.eq(Status.UNREAD)
                        .and(user.email.eq(userEmail)))
                .fetch();
    }

    public List<Notification> findReadNotifications(String userEmail) {
        return jpaQueryFactory
                .selectFrom(notification)
                .innerJoin(notification.user, user)
                .fetchJoin()
                .where(notification.status.eq(Status.READ)
                        .and(user.email.eq(userEmail)))
                .fetch();
    }

    private BooleanExpression lastIdCondition(Long lastId) {
        return lastId != null ? notification.id.lt(lastId) : null;
    }
}
