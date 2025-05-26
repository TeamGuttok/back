package com.app.guttokback.user.application.dto.serviceDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSaveInfo {

    private String password;

    private String email;

    private String nickName;

    private boolean alarm;

    private boolean policyConsent;

    @Builder
    public UserSaveInfo(String password, String email, String nickName, boolean alarm, boolean policyConsent) {
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.alarm = alarm;
        this.policyConsent = policyConsent;
    }
}
