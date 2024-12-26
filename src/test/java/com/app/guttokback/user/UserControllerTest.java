package com.app.guttokback.user;

import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.user.controller.UserController;
import com.app.guttokback.user.dto.controllerDto.UserSaveRequestDto;
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
@WithMockUser
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
    @DisplayName("비밀번호 수정 테스트")
    void testUserPasswordUpdate() throws Exception {
        String newPassword = "newSecurePass123!";

        mockMvc.perform(patch("/api/users/password/{id}/{password}", testId, newPassword)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.PASSWORD_UPDATE_SUCCESS));

        verify(userService).userPasswordUpdate(testId, newPassword);
    }

    @Test
    @DisplayName("닉네임 수정 테스트")
    void testUserNicknameUpdate() throws Exception {
        String newNickName = "UpdatedTester";

        mockMvc.perform(patch("/api/users/nickname/{id}/{nickName}", testId, newNickName)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.NICKNAME_UPDATE_SUCCESS));

        verify(userService).userNicknameUpdate(testId, newNickName);
    }

    @Test
    @DisplayName("알림 수정 테스트")
    void testUserAlarmUpdate() throws Exception {
        mockMvc.perform(patch("/api/users/alarm/{id}", testId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.ALARM_UPDATE_SUCCESS));

        verify(userService).userAlarmUpdate(testId);
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void testUserDelete() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_DELETE_SUCCESS));

        verify(userService).userDelete(testId);
    }
}
