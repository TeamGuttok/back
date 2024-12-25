package com.app.guttokback.user.dto.serviceDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSaveDto {

    private String password;

    private String email;

    private String nickName;

    private boolean alarm;

    @Builder
    public UserSaveDto(String password, String email, String nickName, boolean alarm) {
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.alarm = alarm;
    }
}
