package com.app.guttokback.group.domain;

import com.app.guttokback.global.jpa.AuditInformation;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.PaymentStatus;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "SUBSCRIPTION_GROUPS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionGroupEntity extends AuditInformation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("그룹 생성자")
    private UserEntity user;

    @Column(length = 50, nullable = false)
    @Comment("그룹명")
    private String title;

    @Column(length = 50, nullable = false)
    @Comment("구독 서비스")
    private Subscription subscription;

    @Column(nullable = false)
    @Comment("납부 금액")
    private long paymentAmount;

    @Column(length = 50, nullable = false)
    @Comment("결제 수단")
    private PaymentMethod paymentMethod;

    @Column(length = 50, nullable = false)
    @Comment("결제 여부")
    private PaymentStatus paymentStatus;

    @Column(length = 50, nullable = false)
    @Comment("결제 주기")
    private PaymentCycle paymentCycle;

    @Column(nullable = false)
    @Comment("납부 일")
    private int paymentDay;

    @Column(nullable = true)
    @Comment("리마인드 이메일 발송 예정일")
    private LocalDate reminderDate;

    @Column(length = 250, nullable = true)
    @Comment("공지사항")
    private String notice;

    @Builder
    public SubscriptionGroupEntity(UserEntity user,
                                   String title,
                                   Subscription subscription,
                                   long paymentAmount,
                                   PaymentMethod paymentMethod,
                                   PaymentCycle paymentCycle,
                                   int paymentDay,
                                   String notice
    ) {
        this.user = user;
        this.title = title;
        this.subscription = subscription;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.notice = notice;
    }
}
