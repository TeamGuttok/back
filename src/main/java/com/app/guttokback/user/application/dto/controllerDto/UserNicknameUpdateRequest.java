package com.app.guttokback.user.application.dto.controllerDto;

import com.app.guttokback.user.application.dto.serviceDto.UpdateNicknameInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserNicknameUpdateRequest {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;

    public UserNicknameUpdateRequest(String nickName) {
        this.nickName = nickName;
    }

    public UpdateNicknameInfo updateNicknameDto() {
        return new UpdateNicknameInfo(this.nickName);
    }

}
