package com.app.guttokback.user.application.dto.controllerDto;

import com.app.guttokback.user.application.dto.serviceDto.GetCertificationNumberInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserCertificationNumberRequest {

    @NotBlank(message = "인증번호는 필수 입력 값입니다.")
    private String certificationNumber;

    private String email;

    public UserCertificationNumberRequest(String certificationNumber, String email) {
        this.certificationNumber = certificationNumber;
        this.email = email;
    }

    public GetCertificationNumberInfo getCertificationNumberDto() {
        return new GetCertificationNumberInfo(this.certificationNumber, this.email);
    }
}
