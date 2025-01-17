package com.app.guttokback.user.dto.controllerDto;

import com.app.guttokback.user.dto.serviceDto.GetCertificationNumberDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserCertificationNumberRequestDto {

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
