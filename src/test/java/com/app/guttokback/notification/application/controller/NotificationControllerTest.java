package com.app.guttokback.notification.application.controller;

import com.app.guttokback.common.api.PageOption;
import com.app.guttokback.common.api.PageRequest;
import com.app.guttokback.common.api.PageResponse;
import com.app.guttokback.common.api.ResponseMessages;
import com.app.guttokback.notification.application.dto.NotificationListResponse;
import com.app.guttokback.notification.application.service.NotificationService;
import com.app.guttokback.notification.domain.enums.Category;
import com.app.guttokback.notification.domain.enums.Status;
import com.app.guttokback.user.application.service.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@WithMockUser
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private NotificationService notificationService;
    @MockitoBean
    private SessionService sessionService;

    private final Long testId = 1L;
    private final String testEmail = "test@test.com";

    @Test
    @DisplayName("회원에 대해 생성된 알림 조회 시 정상적으로 응답된다.")
    public void NotificationListTest() throws Exception {
        // given
        PageRequest pageRequest = new PageRequest(testId, 1L);

        NotificationListResponse mockResponse = NotificationListResponse.builder()
                .id(testId)
                .category(Category.APPLICATION)
                .message("test")
                .status(Status.UNREAD)
                .build();

        when(notificationService.list(any(PageOption.class)))
                .thenReturn(PageResponse.of(Collections.singletonList(mockResponse), 1L, false));

        // when & then
        mockMvc.perform(get("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(pageRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").isNotEmpty())
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.hasNext").value(false));

        verify(notificationService).list(any(PageOption.class));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    @DisplayName("회원에 대해 생성된 알림 읽음 업데이트 시 성공 응답을 반환한다.")
    public void notificationUpdateTest() throws Exception {
        mockMvc.perform(put("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.NOTIFICATION_READ_SUCCESS));

        verify(notificationService).statusUpdate(testEmail);
    }

    @Test
    @WithMockUser(username = "test@test.com")
    @DisplayName("회원에 대해 생성된 알림 읽음 업데이트 시 성공 응답을 반환한다.")
    public void notificationDeleteTest() throws Exception {
        mockMvc.perform(delete("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.NOTIFICATION_DELETE_SUCCESS));

        verify(notificationService).delete(testEmail);
    }
}
