package com.app.guttokback.group.domain;

import com.app.guttokback.global.jpa.AuditInformation;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.PaymentStatus;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "SUBSCRIPTION_GROUPS")
public class SubscriptionGroupEntity extends AuditInformation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("그룹 생성자")
    private UserEntity user;

    @Column(length = 50, nullable = false)
    @Comment("그룹명")
    private String name;

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

}
