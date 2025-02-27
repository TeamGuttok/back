package com.app.guttokback.group.application.controller;

import com.app.guttokback.common.api.ApiResponse;
import com.app.guttokback.common.api.ResponseMessages;
import com.app.guttokback.group.application.dto.controllerDto.SubscriptionGroupSaveRequest;
import com.app.guttokback.group.application.service.SubscriptionGroupService;
import com.app.guttokback.user.application.service.SessionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class SubscriptionGroupController {

    private final SubscriptionGroupService subscriptionGroupService;
    private final SessionService sessionService;

    @Hidden
    @PostMapping
    public ResponseEntity<ApiResponse<ResponseMessages>> SubscriptionGroupSave(
            @Valid @RequestBody SubscriptionGroupSaveRequest subscriptionGroupSaveRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        subscriptionGroupService.save(subscriptionGroupSaveRequest.toSave(userDetails.getUsername()));
        sessionService.updateSessionTtl();
        return ApiResponse.success(ResponseMessages.SUBSCRIPTION_GROUP_SAVE_SUCCESS);
    }
}
