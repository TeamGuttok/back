package com.app.guttokback.user.dto.controllerDto;

import com.app.guttokback.user.dto.serviceDto.LoginDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{12,}$",
            message = "비밀번호는 특수문자(@$!%*?&#), 영어 소문자를 포함한 12자리 이상 입력하세요."
    )
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "유효한 이메일 형식이어야 합니다."
    )
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @Builder
    public LoginRequestDto(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public LoginDto loginDto() {
        return LoginDto.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
