package com.app.guttokback.notification.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.apiResponse.PageResponse;
import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.global.apiResponse.util.PageRequest;
import com.app.guttokback.notification.dto.controllerDto.response.NotificationListResponse;
import com.app.guttokback.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "알림 리스트", description = "알림 리스트 요청, 페이징 처리")
    @GetMapping
    public PageResponse<NotificationListResponse> notificationList(
            @Valid PageRequest pageRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return notificationService.list(pageRequest.toListOption(userDetails.getUsername()));
    }

    @Operation(summary = "알림 읽음 처리", description = "알림 상태 READ 변경 요청, 벌크 업데이트")
    @PutMapping
    public ResponseEntity<ApiResponse<ResponseMessages>> notificationUpdate(@AuthenticationPrincipal UserDetails userDetails) {
        notificationService.statusUpdate(userDetails.getUsername());
        return ApiResponse.success(ResponseMessages.NOTIFICATION_READ_SUCCESS);
    }

    @Operation(summary = "알림 삭제", description = "알림 삭제 요청, 벌크 삭제")
    @DeleteMapping
    public ResponseEntity<ApiResponse<ResponseMessages>> notificationDelete(@AuthenticationPrincipal UserDetails userDetails) {
        notificationService.delete(userDetails.getUsername());
        return ApiResponse.success(ResponseMessages.NOTIFICATION_DELETE_SUCCESS);
    }
}
