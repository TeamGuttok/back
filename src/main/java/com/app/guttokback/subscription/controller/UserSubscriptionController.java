package com.app.guttokback.subscription.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.global.apiResponse.util.PageRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.SubscriptionSearchRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionSaveRequest;
import com.app.guttokback.subscription.dto.controllerDto.request.UserSubscriptionUpdateRequest;
import com.app.guttokback.subscription.dto.controllerDto.response.UserSubscriptionListResponse;
import com.app.guttokback.subscription.dto.serviceDto.SubscriptionListInfo;
import com.app.guttokback.subscription.service.UserSubscriptionService;
import com.app.guttokback.user.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final SessionService sessionService;

    @Operation(summary = "구독 항목 생성", description = "구독 항목 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<ResponseMessages>> userSubscriptionSave(
            @Valid @RequestBody UserSubscriptionSaveRequest userSubscriptionSaveRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userSubscriptionService.save(userSubscriptionSaveRequest.toSave(userDetails.getUsername()));
        sessionService.updateSessionTtl();
        return ApiResponse.success(USER_SUBSCRIPTION_SAVE_SUCCESS);
    }

    @Operation(summary = "구독 항목 리스트", description = "구독 항목 리스트 호출, 페이징 처리")
    @GetMapping("/user")
    public PageResponse<UserSubscriptionListResponse> userSubscriptionList(
            @Valid PageRequest pageRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        sessionService.updateSessionTtl();
        return userSubscriptionService.list(pageRequest.toListOption(userDetails.getUsername()));
    }

    @Operation(summary = "구독 항목 수정", description = "구독 항목 수정")
    @PatchMapping("/{userSubscriptionId}")
    public ResponseEntity<ApiResponse<ResponseMessages>> userSubscriptionUpdate(
            @Valid @RequestBody UserSubscriptionUpdateRequest userSubscriptionUpdateRequest,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userSubscriptionId
    ) {
        userSubscriptionService.update(userSubscriptionId, userSubscriptionUpdateRequest.toUpdate(userDetails.getUsername()));
        sessionService.updateSessionTtl();
        return ApiResponse.success(USER_SUBSCRIPTION_UPDATE_SUCCESS);
    }

    @Operation(summary = "구독 항목 삭제", description = "구독 항목 삭제")
    @DeleteMapping("/{userSubscriptionId}")
    public ResponseEntity<ApiResponse<ResponseMessages>> userSubscriptionDelete(
            @PathVariable Long userSubscriptionId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userSubscriptionService.delete(userSubscriptionId, userDetails.getUsername());
        sessionService.updateSessionTtl();
        return ApiResponse.success(USER_SUBSCRIPTION_DELETE_SUCCESS);
    }

    @Operation(summary = "구독 서비스 리스트", description = "구독 서비스 리스트 호출, like 검색")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubscriptionListInfo>>> subscriptionList(SubscriptionSearchRequest searchRequest) {
        List<SubscriptionListInfo> subscription = userSubscriptionService.subscriptionList(searchRequest.toSearch());
        sessionService.updateSessionTtl();
        return ApiResponse.success(SUBSCRIPTION_LIST_SUCCESS, subscription);
    }
}
