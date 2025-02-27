package com.app.guttokback.group.application.controller;

import com.app.guttokback.common.api.ResponseMessages;
import com.app.guttokback.group.application.dto.controllerDto.SubscriptionGroupSaveRequest;
import com.app.guttokback.group.application.dto.serviceDto.SubscriptionGroupSaveInfo;
import com.app.guttokback.group.application.service.SubscriptionGroupService;
import com.app.guttokback.user.application.service.SessionService;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.PaymentMethod;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SubscriptionGroupController.class)
@WithMockUser
class SubscriptionGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SubscriptionGroupService subscriptionGroupService;
    @MockitoBean
    private SessionService sessionService;

    @Test
    @DisplayName("그룹 정보 저장 시 요청 데이터가 성공 응답을 반환한다.")
    public void SubscriptionGroupSaveTest() throws Exception {
        // given
        SubscriptionGroupSaveRequest saveRequest = SubscriptionGroupSaveRequest.builder()
                .title("test")
                .subscription(Subscription.CUSTOM_INPUT)
                .paymentAmount(10000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentCycle(PaymentCycle.MONTHLY)
                .paymentDay(15)
                .notice("test")
                .build();

        // when & then
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessages.SUBSCRIPTION_GROUP_SAVE_SUCCESS));

        verify(subscriptionGroupService).save(any(SubscriptionGroupSaveInfo.class));
    }

}
