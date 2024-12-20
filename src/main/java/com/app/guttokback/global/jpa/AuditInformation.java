package com.app.guttokback.global.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditInformation {
    @CreatedDate
    @Comment("등록일")
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private LocalDateTime registerDate;

    @LastModifiedDate
    @Comment("수정일")
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
}
