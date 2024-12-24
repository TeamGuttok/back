package com.app.guttokback.user;

import com.app.guttokback.user.controller.UserController;
import com.app.guttokback.user.dto.controllerDto.UserSaveRequestDto;
import com.app.guttokback.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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

    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";
    private final String testNickName = "Tester";

    @BeforeEach
    void setUp() {
        // Mock Service 동작 설정
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
                .andExpect(content().string("유저 저장 성공"));

        verify(userService).userSave(any());
    }

    @Test
    @DisplayName("비밀번호 수정 테스트")
    void testUserPasswordUpdate() throws Exception {
        String newPassword = "newPassword123";

        mockMvc.perform(patch("/api/users/signup/password/{email}/{password}", testEmail, newPassword)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("비밀번호 수정 성공"));

        verify(userService).userPasswordUpdate(testEmail, newPassword);
    }

    @Test
    @DisplayName("닉네임 수정 테스트")
    void testUserNicknameUpdate() throws Exception {
        String newNickName = "UpdatedTester";

        mockMvc.perform(patch("/api/users/signup/nickname/{email}/{nickName}", testEmail, newNickName)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("닉네임 수정 성공"));

        verify(userService).userNicknameUpdate(testEmail, newNickName);
    }

    @Test
    @DisplayName("알림 수정 테스트")
    void testUserAlarmUpdate() throws Exception {
        mockMvc.perform(patch("/api/users/signup/alarm/{email}", testEmail)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("알림 수정 성공"));

        verify(userService).userAlarmUpdate(testEmail);
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void testUserDelete() throws Exception {
        mockMvc.perform(delete("/api/users/signup/{email}", testEmail)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("유저 삭제 성공"));

        verify(userService).userDelete(testEmail);
    }
}
