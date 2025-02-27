package com.app.guttokback.user.application.dto.serviceDto;

import lombok.Getter;

@Getter
public class UpdatePasswordInfo {
    private String password;

    public UpdatePasswordInfo(String password) {
        this.password = password;
    }
}
