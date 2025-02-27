package com.app.guttokback.notification.application.dto;

import com.app.guttokback.notification.domain.enums.Category;
import com.app.guttokback.notification.domain.enums.Status;
import com.app.guttokback.notification.domain.model.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationListResponse {

    private Long id;

    private Category category;

    private String message;

    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime registerDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateDate;

    @Builder
    public NotificationListResponse(Long id,
                                    Category category,
                                    String message,
                                    Status status,
                                    LocalDateTime registerDate,
                                    LocalDateTime updateDate
    ) {
        this.id = id;
        this.category = category;
        this.message = message;
        this.status = status;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    public static NotificationListResponse of(Notification notification) {
        return NotificationListResponse.builder()
                .id(notification.getId())
                .category(notification.getCategory())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .registerDate(notification.getRegisterDate())
                .updateDate(notification.getUpdateDate())
                .build();
    }
}
