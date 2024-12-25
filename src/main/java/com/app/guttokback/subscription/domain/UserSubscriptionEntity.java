package com.app.guttokback.subscription.domain;

import com.app.guttokback.global.jpa.AuditInformation;
import com.app.guttokback.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "USER_SUBSCRIPTIONS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionEntity extends AuditInformation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 50, nullable = true)
    @Comment("사용자 지정 명")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private SubscriptionEntity subscription;

    @Column(nullable = false)
    @Comment("납부 금액")
    private long paymentAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("결제 수단")
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("결제 여부")
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    @Comment("첫 납부 날짜")
    private LocalDate startDate;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("결제 주기")
    private PaymentCycle paymentCycle;

    @Column(nullable = false)
    @Comment("납부 일")
    private int paymentDay;

    @Column(length = 250, nullable = true)
    @Comment("사용자 메모")
    private String memo;

}
