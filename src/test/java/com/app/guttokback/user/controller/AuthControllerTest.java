package com.app.guttokback.user.controller;

import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.global.security.config.SecurityConfig;
import com.app.guttokback.user.dto.controllerDto.LoginRequestDto;
import com.app.guttokback.user.dto.serviceDto.UserDetailDto;
import com.app.guttokback.user.repository.UserRepository;
import com.app.guttokback.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    private final String testEmail = "test@example.com";
    private final String testPassword = "securePass123!";

    @BeforeEach
    void setUp() {
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        when(mockAuthentication.getName()).thenReturn(testEmail);

        when(userService.userDetail(testEmail)).thenReturn(UserDetailDto.builder()
                .email(testEmail)
                .nickName("testNick")
                .alarm(true)
                .build());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void signin_success() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(testEmail, testPassword);

        mockMvc.perform(post("/api/users/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_LOGIN_SUCCESS))
                .andExpect(jsonPath("$.data").value("testNick"));
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void signout_Success() throws Exception {
        // when & then
        mockMvc.perform(post("/api/users/signout")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_LOGOUT_SUCCESS));
    }
}
