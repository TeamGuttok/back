package com.app.guttokback.subscription.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionSaveRequest;
import com.app.guttokback.subscription.service.UserSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.app.guttokback.global.apiResponse.ResponseMessages.USER_SUBSCRIPTION_SAVE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> userSubscriptionSave(
            @Valid @RequestBody UserSubscriptionSaveRequest userSubscriptionSaveRequest
    ) {
        userSubscriptionService.save(userSubscriptionSaveRequest.toSave());
        return ApiResponse.success(USER_SUBSCRIPTION_SAVE_SUCCESS);
    }
}
