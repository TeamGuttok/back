package com.app.guttokback.email.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailType {
    REMINDER("리마인드");

    private final String type;
}
