package com.app.guttokback.email.application.dto.controllerDto;

import com.app.guttokback.email.application.dto.serviceDto.GetEmailInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserEmailRequest {

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "유효한 이메일 형식이어야 합니다."
    )
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    public UserEmailRequest(String email) {
        this.email = email;
    }

    public GetEmailInfo getEmailDto() {
        return new GetEmailInfo(this.email);
    }
}
