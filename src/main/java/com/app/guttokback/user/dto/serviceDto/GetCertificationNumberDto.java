package com.app.guttokback.user.dto.serviceDto;

import lombok.Getter;

@Getter
public class GetCertificationNumberDto {

    private String certificationNumber;

    private String email;

    public GetCertificationNumberDto(String certificationNumber, String email) {
        this.certificationNumber = certificationNumber;
        this.email = email;
    }
}
