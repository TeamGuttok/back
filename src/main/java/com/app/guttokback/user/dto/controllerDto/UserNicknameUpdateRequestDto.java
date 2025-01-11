package com.app.guttokback.user.dto.controllerDto;

import com.app.guttokback.user.dto.serviceDto.UpdateNicknameDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserNicknameUpdateRequestDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;

    public UserNicknameUpdateRequestDto(String nickName) {
        this.nickName = nickName;
    }

    public UpdateNicknameDto updateNicknameDto() {
        return new UpdateNicknameDto(this.nickName);
    }

}
