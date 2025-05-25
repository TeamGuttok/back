package com.app.guttokback.common.security;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    ROLE_USER,
    ROLE_TEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
