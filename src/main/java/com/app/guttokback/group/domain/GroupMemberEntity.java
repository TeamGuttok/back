package com.app.guttokback.group.domain;

import com.app.guttokback.global.jpa.AuditInformation;
import com.app.guttokback.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "GROUP_MEMBERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMemberEntity extends AuditInformation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private SubscriptionGroupEntity subscriptionGroup;

    @Builder
    public GroupMemberEntity(UserEntity user, SubscriptionGroupEntity subscriptionGroup) {
        this.user = user;
        this.subscriptionGroup = subscriptionGroup;
    }
}
