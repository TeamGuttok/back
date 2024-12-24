package com.app.guttokback.user.dto.serviceDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailDto {

    private String email;

    private String nickName;

    private boolean alarm;

    @Builder
    public UserDetailDto(String email, String nickName, boolean alarm) {
        this.email = email;
        this.nickName = nickName;
        this.alarm = alarm;
    }
}
