package com.app.guttokback.notification.controller;

import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.apiResponse.util.PageRequest;
import com.app.guttokback.notification.dto.controllerDto.response.NotificationListResponse;
import com.app.guttokback.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class notificationController {

    private final NotificationService notificationService;

    @GetMapping
    public PageResponse<NotificationListResponse> notificationList(
            @Valid PageRequest pageRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return notificationService.list(pageRequest.toListOption(userDetails.getUsername()));
    }
}
