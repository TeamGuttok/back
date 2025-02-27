package com.app.guttokback.user.application.service;

import com.app.guttokback.common.exception.CustomApplicationException;
import com.app.guttokback.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userService);

    @Test
    @DisplayName("로그인 테스트 - 이메일 찾기 실패")
    void signin_EMAIL_NOT_FOUND() {
        // given
        String email = "notfound@example.com";
        when(userService.findByUserEmail(email)).thenThrow(new CustomApplicationException(ErrorCode.EMAIL_NOT_FOUND));

        // when
        CustomApplicationException exception = assertThrows(CustomApplicationException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });

        // then
        assert exception.getErrorCode() == ErrorCode.EMAIL_NOT_FOUND;
    }
}
