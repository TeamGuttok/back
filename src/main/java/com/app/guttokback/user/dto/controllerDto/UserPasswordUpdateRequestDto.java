package com.app.guttokback.user.dto.controllerDto;

import com.app.guttokback.user.dto.serviceDto.UpdatePasswordDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPasswordUpdateRequestDto {
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{12,}$",
            message = "비밀번호는 특수문자(@$!%*?&#), 영어 소문자를 포함한 12자리 이상 입력하세요."
    )
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    public UserPasswordUpdateRequestDto(String password) {
        this.password = password;
    }

    public UpdatePasswordDto updatePasswordDto() {
        return new UpdatePasswordDto(this.password);
    }
}