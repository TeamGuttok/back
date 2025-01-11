package com.app.guttokback.user.dto.serviceDto;

import lombok.Getter;

@Getter
public class UpdatePasswordDto {
    private String password;

    public UpdatePasswordDto(String password) {
        this.password = password;
    }
}
