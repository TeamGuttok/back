package com.app.guttokback.global.aws.ses.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.aws.ses.service.RemainderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.app.guttokback.global.apiResponse.ResponseMessages.MAIL_SEND_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class EmailController {

    private final RemainderService remainderService;

    @PostMapping("/reminder")
    public ResponseEntity<ApiResponse<String>> sendReminder() {
        remainderService.sendReminder();
        return ApiResponse.success(MAIL_SEND_SUCCESS);
    }
}

// 테스트를 위한 임시 컨트롤러 → 스케줄러 적용 후 제거
