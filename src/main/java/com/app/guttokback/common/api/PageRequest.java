package com.app.guttokback.common.api;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageRequest {

    @Positive(message = "데이터의 id는 양수여야 합니다.")
    private Long lastId;

    @Positive(message = "한 페이지에 조회 할 데이터 수는 양수여야 합니다.")
    private long size;

    public PageRequest(Long lastId, Long size) {
        this.lastId = lastId;
        this.size = size == null ? 10L : size;
    }

    public PageOption toListOption(String userEmail) {
        return new PageOption(userEmail, lastId, size);
    }
}
