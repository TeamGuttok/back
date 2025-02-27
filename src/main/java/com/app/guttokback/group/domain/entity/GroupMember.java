package com.app.guttokback.group.domain.entity;

import com.app.guttokback.common.domain.BaseEntity;
import com.app.guttokback.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "GROUP_MEMBERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private SubscriptionGroup subscriptionGroup;

    @Builder
    public GroupMember(User user, SubscriptionGroup subscriptionGroup) {
        this.user = user;
        this.subscriptionGroup = subscriptionGroup;
    }
}
