package com.app.guttokback.notification.domain;

import com.app.guttokback.global.jpa.AuditInformation;
import com.app.guttokback.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "NOTIFICATIONS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity extends AuditInformation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 50, nullable = false)
    @Comment("카테고리")
    private Category category;

    @Column(length = 250, nullable = false)
    @Comment("내용")
    private String message;

    @Column(length = 50, nullable = false)
    @Comment("상태")
    private Status status;

    @Builder
    public NotificationEntity(UserEntity user, Category category, String message, Status status) {
        this.user = user;
        this.category = category;
        this.message = message;
        this.status = status;
    }
}
