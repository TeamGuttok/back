package com.app.guttokback.group.domain;

import com.app.guttokback.global.jpa.AuditInformation;
import com.app.guttokback.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "GROUP_MEMBERS")
public class GroupMemberEntity extends AuditInformation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private SubscriptionGroupEntity subscriptionGroup;
}
