package com.app.guttokback.user.dto.serviceDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailDto {

    private Long pk;

    private String email;

    private String nickName;

    private boolean alarm;

    @Builder
    public UserDetailDto(Long pk, String email, String nickName, boolean alarm) {
        this.pk = pk;
        this.email = email;
        this.nickName = nickName;
        this.alarm = alarm;
    }
}
