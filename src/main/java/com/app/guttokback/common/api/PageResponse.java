package com.app.guttokback.common.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponse<T> {
    private List<T> contents;
    private long size;
    private boolean hasNext;

    public PageResponse(List<T> contents, long size, boolean hasNext) {
        this.contents = contents;
        this.size = size;
        this.hasNext = hasNext;
    }

    public static <T> PageResponse<T> of(List<T> contents, long size, boolean hasNext) {
        return new PageResponse<>(contents, size, hasNext);
    }
}
