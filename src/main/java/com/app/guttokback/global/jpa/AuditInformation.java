package com.app.guttokback.global.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreationTimestamp
    @Comment("등록일")
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private LocalDateTime registerDate;

    @UpdateTimestamp
    @Comment("수정일")
    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDateTime updateDate;
}
