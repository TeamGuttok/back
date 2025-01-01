package com.app.guttokback.subscription.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionListRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionSaveRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionUpdateRequest;
import com.app.guttokback.subscription.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.subscription.service.UserSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.app.guttokback.global.apiResponse.ResponseMessages.USER_SUBSCRIPTION_SAVE_SUCCESS;
import static com.app.guttokback.global.apiResponse.ResponseMessages.USER_SUBSCRIPTION_UPDATE_SUCCESS;

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

    @GetMapping("/{userId}")
    public PageResponse<UserSubscriptionListResponse> userSubscriptionList(
            @Valid UserSubscriptionListRequest userSubscriptionListRequest,
            @PathVariable Long userId
    ) {
        return userSubscriptionService.list(userSubscriptionListRequest.toListOption(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> userSubscriptionUpdate(
            @Valid @RequestBody UserSubscriptionUpdateRequest userSubscriptionUpdateRequest,
            @PathVariable Long id
    ) {
        userSubscriptionService.update(id, userSubscriptionUpdateRequest.toUpdate());
        return ApiResponse.success(USER_SUBSCRIPTION_UPDATE_SUCCESS);
    }
}
