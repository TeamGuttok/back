package com.app.guttokback.user.entity;

import com.app.guttokback.global.jpa.AuditInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;


@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends AuditInformation {
    @Id
    @Comment("아이디")
    private Long id;

    @Column(length = 100, nullable = false)
    @Comment("비밀번호")
    private String password;

    @Column(length = 100, nullable = false)
    @Comment("이메일")
    private String email;

    @Column(length = 50, nullable = false)
    @Comment("이름")
    private String name;

    @Column(length = 50, nullable = false)
    @Comment("닉네임")
    private String nickName;

    @Column(length = 50, nullable = false)
    @Comment("생년월일")
    private String birth;

    @Column(nullable = false)
    @Comment("알림여부")
    private boolean alarm;


}
