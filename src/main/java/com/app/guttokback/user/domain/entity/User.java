package com.app.guttokback.user.domain.entity;

import com.app.guttokback.common.domain.BaseEntity;
import com.app.guttokback.common.security.Roles;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class User extends BaseEntity implements UserDetails {

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Comment("사용자 권한")
    private List<Roles> roles = new ArrayList<>();

    @Builder
    public User(String password, String email, String nickName, boolean alarm, Roles role) {
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.alarm = alarm;
        this.roles.add(role);
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
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
