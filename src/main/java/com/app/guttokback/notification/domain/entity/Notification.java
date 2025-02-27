package com.app.guttokback.notification.domain.entity;

import com.app.guttokback.common.domain.BaseEntity;
import com.app.guttokback.notification.domain.enums.Category;
import com.app.guttokback.notification.domain.enums.Status;
import com.app.guttokback.user.domain.entity.User;
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
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
    public Notification(User user, Category category, String message, Status status) {
        this.user = user;
        this.category = category;
        this.message = message;
        this.status = status;
    }

    public void statusUpdate(Status status) {
        this.status = status;
    }
}
