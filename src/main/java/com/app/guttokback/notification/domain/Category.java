package com.app.guttokback.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    REMINDER("리마인드");

    private final String category;

}
