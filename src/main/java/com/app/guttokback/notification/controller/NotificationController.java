package com.app.guttokback.notification.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.global.apiResponse.util.PageRequest;
import com.app.guttokback.notification.dto.controllerDto.response.NotificationListResponse;
import com.app.guttokback.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public PageResponse<NotificationListResponse> notificationList(
            @Valid PageRequest pageRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return notificationService.list(pageRequest.toListOption(userDetails.getUsername()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> notificationUpdate(@AuthenticationPrincipal UserDetails userDetails) {
        notificationService.statusUpdate(userDetails.getUsername());
        return ApiResponse.success(ResponseMessages.NOTIFICATION_READ_SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> notificationDelete(@AuthenticationPrincipal UserDetails userDetails) {
        notificationService.delete(userDetails.getUsername());
        return ApiResponse.success(ResponseMessages.NOTIFICATION_DELETE_SUCCESS);
    }
}
