package com.app.guttokback.subscription.dto.controllerDto.request;

import com.app.guttokback.subscription.dto.serviceDto.UserSubscriptionListInfo;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSubscriptionListRequest {

    @Positive(message = "데이터의 id는 양수여야 합니다.")
    private Long lastId;

    @Positive(message = "한 페이지에 조회 할 데이터 수는 양수여야 합니다.")
    private long size = 10;

    public UserSubscriptionListInfo toListOption(Long userId) {
        return new UserSubscriptionListInfo(userId, lastId, size);
    }
}
