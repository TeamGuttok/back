package com.app.guttokback.common.api;

import lombok.Getter;

@Getter
public class PageOption {

    private final String userEmail;

    private final Long lastId;

    private final long size;

    public PageOption(String userEmail, Long lastId, long size) {
        this.userEmail = userEmail;
        this.lastId = lastId;
        this.size = size;
    }
}
