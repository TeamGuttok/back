package com.app.guttokback.user.application.dto.serviceDto;

import lombok.Getter;

@Getter
public class GetCertificationNumberInfo {

    private String certificationNumber;

    private String email;

    public GetCertificationNumberInfo(String certificationNumber, String email) {
        this.certificationNumber = certificationNumber;
        this.email = email;
    }
}
