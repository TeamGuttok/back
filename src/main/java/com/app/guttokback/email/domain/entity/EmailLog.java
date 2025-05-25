package com.app.guttokback.email.domain.entity;

import com.app.guttokback.common.domain.BaseEntity;
import com.app.guttokback.email.domain.enums.EmailType;
import com.app.guttokback.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "email_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50, nullable = false)
    @Comment("이메일 타입")
    private EmailType emailType;

    @Column(nullable = false)
    @Comment("이메일 발송 횟수")
    private long count;

    @Builder
    public EmailLog(User user, EmailType emailType, long count) {
        this.user = user;
        this.emailType = emailType;
        this.count = count;
    }

    public void increaseCount() {
        count += 1;
    }
}
