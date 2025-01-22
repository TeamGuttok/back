package com.app.guttokback.subscription.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.subscription.dto.controllerDto.request.SubscriptionSearchRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionListRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionSaveRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionUpdateRequest;
import com.app.guttokback.subscription.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.subscription.dto.serviceDto.SubscriptionListInfo;
import com.app.guttokback.subscription.service.UserSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.app.guttokback.global.apiResponse.ResponseMessages.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> userSubscriptionSave(
            @Valid @RequestBody UserSubscriptionSaveRequest userSubscriptionSaveRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userSubscriptionService.save(userSubscriptionSaveRequest.toSave(userDetails.getUsername()));
        return ApiResponse.success(USER_SUBSCRIPTION_SAVE_SUCCESS);
    }

    @GetMapping("/user")
    public PageResponse<UserSubscriptionListResponse> userSubscriptionList(
            @Valid UserSubscriptionListRequest userSubscriptionListRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return userSubscriptionService.list(userSubscriptionListRequest.toListOption(userDetails.getUsername()));
    }

    @PatchMapping("/{userSubscriptionId}")
    public ResponseEntity<ApiResponse<Object>> userSubscriptionUpdate(
            @Valid @RequestBody UserSubscriptionUpdateRequest userSubscriptionUpdateRequest,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userSubscriptionId
    ) {
        userSubscriptionService.update(userSubscriptionId, userSubscriptionUpdateRequest.toUpdate(userDetails.getUsername()));
        return ApiResponse.success(USER_SUBSCRIPTION_UPDATE_SUCCESS);
    }

    @DeleteMapping("/{userSubscriptionId}")
    public ResponseEntity<ApiResponse<Object>> userSubscriptionDelete(
            @PathVariable Long userSubscriptionId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userSubscriptionService.delete(userSubscriptionId, userDetails.getUsername());
        return ApiResponse.success(USER_SUBSCRIPTION_DELETE_SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> subscriptionList(SubscriptionSearchRequest searchRequest) {
        List<SubscriptionListInfo> subscription = userSubscriptionService.subscriptionList(searchRequest.toSearch());
        return ApiResponse.success(SUBSCRIPTION_LIST_SUCCESS, subscription);
    }
}
