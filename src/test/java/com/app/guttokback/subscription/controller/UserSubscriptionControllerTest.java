package com.app.guttokback.subscription.controller;

import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.PaymentStatus;
import com.app.guttokback.subscription.domain.Subscription;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionListRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionSaveRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionUpdateRequest;
import com.app.guttokback.subscription.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.subscription.dto.serviceDto.*;
import com.app.guttokback.subscription.service.UserSubscriptionService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserSubscriptionController.class)
@WithMockUser
class UserSubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserSubscriptionService userSubscriptionService;

    private final Long testId = 1L;
    private final String testEmail = "example@example.com";

    @Test
    @DisplayName("사용자 구독정보 저장 시 요청 데이터가 성공 응답을 반환한다.")
    public void userSubscriptionSaveTest() throws Exception {
        // given
        UserSubscriptionSaveRequest saveRequest = UserSubscriptionSaveRequest.builder()
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(1)
                .memo("test")
                .build();

        // when & then
        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_SUBSCRIPTION_SAVE_SUCCESS));

        verify(userSubscriptionService).save(any(UserSubscriptionSaveInfo.class));
    }

    @Test
    @DisplayName("사용자 구독정보 조회 시 요청 데이터가 성공 응답을 반환한다.")
    public void userSubscriptionListTest() throws Exception {
        // given
        UserSubscriptionListRequest listRequest = new UserSubscriptionListRequest(testId, 1L);

        UserSubscriptionListResponse mockResponse = UserSubscriptionListResponse.builder()
                .title("test")
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(1)
                .build();

        when(userSubscriptionService.list(any(UserSubscriptionListInfo.class)))
                .thenReturn(PageResponse.of(Collections.singletonList(mockResponse), 1L, false));

        // when & then
        mockMvc.perform(get("/api/subscriptions/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(listRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").isNotEmpty())
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.hasNext").value(false));

        verify(userSubscriptionService).list(any(UserSubscriptionListInfo.class));
    }

    @Test
    @DisplayName("사용자 구독정보 수정 시 요청 데이터가 성공 응답을 반환한다.")
    public void userSubscriptionUpdateTest() throws Exception {
        // given
        UserSubscriptionUpdateRequest updateRequest = UserSubscriptionUpdateRequest.builder()
                .title("update")
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentStatus(PaymentStatus.COMPLETED)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(1)
                .memo("update")
                .build();

        // when & then
        mockMvc.perform(patch("/api/subscriptions/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_SUBSCRIPTION_UPDATE_SUCCESS));

        verify(userSubscriptionService).update(eq(testId), any(UserSubscriptionUpdateInfo.class));
    }

    @Test
    @WithMockUser(username = "example@example.com")
    @DisplayName("사용자 구독정보 삭제 시 요청 데이터가 성공 응답을 반환한다.")
    public void userSubscriptionDeleteTest() throws Exception {
        // given
        UserSubscriptionSaveRequest saveRequest = UserSubscriptionSaveRequest.builder()
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(1)
                .memo("test")
                .build();

        // when & then
        mockMvc.perform(delete("/api/subscriptions/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.USER_SUBSCRIPTION_DELETE_SUCCESS));

        verify(userSubscriptionService).delete(testId, testEmail);
    }

    @Test
    @DisplayName("구독 서비스 조회 시 성공 응답을 반환한다.")
    public void subscriptionListTest() throws Exception {
        // given
        List<SubscriptionListInfo> mockResponse = Collections.singletonList(
                new SubscriptionListInfo("YOUTUBE_PREMIUM", "유튜브 프리미엄")
        );

        when(userSubscriptionService.subscriptionList(any(SubscriptionSearchInfo.class))).thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.SUBSCRIPTION_LIST_SUCCESS))
                .andExpect(jsonPath("$.data[0].code").value("YOUTUBE_PREMIUM"))
                .andExpect(jsonPath("$.data[0].name").value("유튜브 프리미엄"));

        verify(userSubscriptionService).subscriptionList(any(SubscriptionSearchInfo.class));
    }
}
