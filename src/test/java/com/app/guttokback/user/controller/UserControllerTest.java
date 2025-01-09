package com.app.guttokback.user.controller;

import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import com.app.guttokback.user.dto.controllerDto.UserSaveRequestDto;
import com.app.guttokback.user.dto.serviceDto.UserDetailDto;
import com.app.guttokback.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private final Long testId = 1L;
    private final String testEmail = "test@example.com";
    private final String testPassword = "securePass123!";
    private final String testNickName = "Tester";

    @BeforeEach
    void setUp() {
        doNothing().when(userService).userSave(any());
        doNothing().when(userService).userDelete(any());
        doNothing().when(userService).userPasswordUpdate(any(), any());
        doNothing().when(userService).userNicknameUpdate(any(), any());
        doNothing().when(userService).userAlarmUpdate(any());
    }

    @Test
    @WithMockUser
    @DisplayName("유저 저장 테스트")
    void testUserSave() throws Exception {
        UserSaveRequestDto requestDto = UserSaveRequestDto.builder()
                .email(testEmail)
                .password(testPassword)
                .nickName(testNickName)
                .alarm(true)
                .build();

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_SAVE_SUCCESS));

        verify(userService).userSave(any());
    }

    @Test
    @WithMockUser
    @DisplayName("회원 저장 실패 - 중복 이메일")
    void testUserSaveFailDueToDuplicateEmail() throws Exception {
        doThrow(new CustomApplicationException(ErrorCode.EMAIL_SAME_FOUND))
                .when(userService).userSave(any());

        UserSaveRequestDto requestDto = UserSaveRequestDto.builder()
                .email(testEmail)
                .password(testPassword)
                .nickName(testNickName)
                .alarm(true)
                .build();

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(ErrorCode.EMAIL_SAME_FOUND.getMessage()));

        verify(userService).userSave(any());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @DisplayName("비밀번호 수정 테스트")
    void testUserPasswordUpdate() throws Exception {
        String newPassword = "newSecurePass123!";

        mockMvc.perform(patch("/api/users/password/{password}", newPassword)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.PASSWORD_UPDATE_SUCCESS));

        verify(userService).userPasswordUpdate(testEmail, newPassword);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @DisplayName("닉네임 수정 테스트")
    void testUserNicknameUpdate() throws Exception {
        String newNickName = "UpdatedTester";

        mockMvc.perform(patch("/api/users/nickname/{nickName}", newNickName)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.NICKNAME_UPDATE_SUCCESS));

        verify(userService).userNicknameUpdate(testEmail, newNickName);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @DisplayName("알림 수정 테스트")
    void testUserAlarmUpdate() throws Exception {
        mockMvc.perform(patch("/api/users/alarm")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.ALARM_UPDATE_SUCCESS));

        verify(userService).userAlarmUpdate(testEmail);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @DisplayName("유저 삭제 테스트")
    void testUserDelete() throws Exception {
        mockMvc.perform(delete("/api/users")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_DELETE_SUCCESS));

        verify(userService).userDelete(testEmail);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @DisplayName("회원 조회 성공 테스트")
    void testUserDetailSuccess() throws Exception {
        UserDetailDto userDetailDto = UserDetailDto.builder()
                .id(testId)
                .email(testEmail)
                .nickName(testNickName)
                .alarm(true)
                .build();

        when(userService.userDetail(testEmail)).thenReturn(userDetailDto);

        mockMvc.perform(get("/api/users")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_RETRIEVE_SUCCESS))
                .andExpect(jsonPath("$.data.id").value(testId))
                .andExpect(jsonPath("$.data.email").value(testEmail))
                .andExpect(jsonPath("$.data.nickName").value(testNickName))
                .andExpect(jsonPath("$.data.alarm").value(true));

        verify(userService).userDetail(testEmail);
    }
}
