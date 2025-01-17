package com.app.guttokback.global.aws.ses.dto.serviceDto;

import lombok.Getter;

@Getter
public class GetEmailDto {
    private String email;

    public GetEmailDto(String email) {
        this.email = email;
    }
}
