package com.app.guttokback.user.dto.serviceDto;

import lombok.Getter;

@Getter
public class UpdateNicknameDto {
    private String nickName;

    public UpdateNicknameDto(String nickName) {
        this.nickName = nickName;
    }
}
