package com.app.guttokback.notification.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    APPLICATION("애플리케이션"),
    REMINDER("리마인드");

    private final String category;

}
