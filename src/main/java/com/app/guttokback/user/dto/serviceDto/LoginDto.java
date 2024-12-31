package com.app.guttokback.user.dto.serviceDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {

    private String password;

    private String email;

    @Builder
    public LoginDto(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
