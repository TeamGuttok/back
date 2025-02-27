package com.app.guttokback.email.application.dto.serviceDto;

import lombok.Getter;

@Getter
public class GetEmailInfo {
    private String email;

    public GetEmailInfo(String email) {
        this.email = email;
    }
}
