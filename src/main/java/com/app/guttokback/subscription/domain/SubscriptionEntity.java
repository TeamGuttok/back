package com.app.guttokback.subscription.domain;

import com.app.guttokback.global.jpa.AuditInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "SUBSCRIPTIONS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionEntity extends AuditInformation {

    @Column(length = 50, nullable = false)
    private String name;

    @Builder
    public SubscriptionEntity(String name) {
        this.name = name;
    }
}
