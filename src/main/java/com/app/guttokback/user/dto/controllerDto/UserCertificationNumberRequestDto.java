package com.app.guttokback.user.dto.controllerDto;

import com.app.guttokback.user.dto.serviceDto.GetCertificationNumberDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserCertificationNumberRequestDto {

    @Pattern(
            regexp = "[A-Za-z0-9]{4}$",
            message = "유효한 인증번호 형식이어야 합니다."
    )
    @NotBlank(message = "인증번호는 필수 입력 값입니다.")
    private String certificationNumber;

    private String email;

    public UserCertificationNumberRequestDto(String certificationNumber, String email) {
        this.certificationNumber = certificationNumber;
        this.email = email;
    }

    public GetCertificationNumberDto getCertificationNumberDto() {
        return new GetCertificationNumberDto(this.certificationNumber, this.email);
    }
}
