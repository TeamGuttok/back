package com.app.guttokback.user.application.dto.serviceDto;

import lombok.Getter;

@Getter
public class UpdateNicknameInfo {
    private String nickName;

    public UpdateNicknameInfo(String nickName) {
        this.nickName = nickName;
    }
}
