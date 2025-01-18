package com.app.guttokback.user.domain;

import com.app.guttokback.global.jpa.AuditInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE USERS SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class UserEntity extends AuditInformation implements UserDetails {

    @Column(length = 100, nullable = false)
    @Comment("비밀번호")
    private String password;

    @Column(length = 100, nullable = false, unique = true)
    @Comment("이메일")
    private String email;

    @Column(length = 50, nullable = false)
    @Comment("닉네임")
    private String nickName;

    @Column(nullable = false)
    @Comment("알림여부")
    private boolean alarm;

    @Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT false")
    @Comment("논리적삭제")
    private boolean delete;

    @Builder
    public UserEntity(String password, String email, String nickName, boolean alarm) {
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.alarm = alarm;
    }

    public void passwordChange(String password) {
        this.password = password;
    }

    public void nickNameChange(String nickName) {
        this.nickName = nickName;
    }

    public void alarmChange() {
        this.alarm = !this.alarm;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
