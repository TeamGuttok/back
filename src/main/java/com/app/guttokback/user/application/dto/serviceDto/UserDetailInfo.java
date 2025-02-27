package com.app.guttokback.user.application.dto.serviceDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailInfo {

    private Long id;

    private String email;

    private String nickName;

    private boolean alarm;

    @Builder
    public UserDetailInfo(Long id, String email, String nickName, boolean alarm) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.alarm = alarm;
    }
}
