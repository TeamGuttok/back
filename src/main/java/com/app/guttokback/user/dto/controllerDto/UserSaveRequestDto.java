package com.app.guttokback.user.dto.controllerDto;

import com.app.guttokback.user.dto.serviceDto.UserSaveDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSaveRequestDto {

        @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.")
        private String password;

        @NotEmpty(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        private String email;

        @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
        private String nickName;

        private boolean alarm;

        public UserSaveDto userSaveDto() {
                return UserSaveDto.builder()
                        .email(this.email)
                        .password(this.password)
                        .nickName(this.nickName)
                        .alarm(this.alarm)
                        .build();
        }

}
