package com.app.guttokback.user.application.dto.controllerDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @Builder
    public LoginRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
