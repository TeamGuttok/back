package com.app.guttokback.subscription.domain;

import com.app.guttokback.global.jpa.AuditInformation;
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
@Table(name = "USER_SUBSCRIPTIONS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscriptionEntity extends AuditInformation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 50, nullable = false)
    @Comment("구독 서비스")
    private Subscription subscription;

    @Column(length = 50, nullable = true)
    @Comment("사용자 지정 명")
    private String title;

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
    @Comment("사용자 메모")
    private String memo;

    @Builder
    public UserSubscriptionEntity(UserEntity user,
                                  String title,
                                  Subscription subscription,
                                  long paymentAmount,
                                  PaymentMethod paymentMethod,
                                  PaymentCycle paymentCycle,
                                  int paymentDay,
                                  String memo
    ) {
        this.user = user;
        this.title = title;
        this.subscription = subscription;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.memo = memo;
    }

    public void update(String title,
                       long paymentAmount,
                       PaymentMethod paymentMethod,
                       PaymentStatus paymentStatus,
                       PaymentCycle paymentCycle,
                       int paymentDay,
                       String memo
    ) {
        this.title = title;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentCycle = paymentCycle;
        this.paymentDay = paymentDay;
        this.memo = memo;
    }

    public void updateReminderDate() {
        // 주기가 년월주에 따라서 reminderDate를 업데이트하는로직
        int paymentDay = this.getPaymentDay();
        LocalDate now = LocalDate.now();
        LocalDate remainderSendDateTime = now.withDayOfMonth(paymentDay);

        remainderSendDateTime = switch (this.getPaymentCycle()) {
            case PaymentCycle.YEARLY -> remainderSendDateTime.plusYears(1);
            case PaymentCycle.MONTHLY -> remainderSendDateTime.plusMonths(1);
            case PaymentCycle.WEEKLY -> remainderSendDateTime.plusWeeks(1);
        };
        this.reminderDate = remainderSendDateTime;
    }
}
