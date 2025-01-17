package com.app.guttokback.global.aws.ses.dto.controllerDto;

import com.app.guttokback.global.aws.ses.dto.serviceDto.GetEmailDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserEmailRequestDto {

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "유효한 이메일 형식이어야 합니다."
    )
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    public UserEmailRequestDto(String email) {
        this.email = email;
    }

    public GetEmailDto getEmailDto() {
        return new GetEmailDto(this.email);
    }
}
