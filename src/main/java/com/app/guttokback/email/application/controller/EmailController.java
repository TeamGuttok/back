package com.app.guttokback.email.application.controller;

import com.app.guttokback.common.api.ApiResponse;
import com.app.guttokback.common.api.ResponseMessages;
import com.app.guttokback.email.application.dto.controllerDto.UserEmailRequest;
import com.app.guttokback.email.application.service.CertificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.app.guttokback.common.api.ResponseMessages.CERTIFICATION_EMAIL_SEND_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class EmailController {

    private final CertificationService certificationService;

    @Operation(summary = "인증번호 이메일 발송", description = "인증번호 이메일 발송 요청")
    @PostMapping("/certification")
    public ResponseEntity<ApiResponse<ResponseMessages>> userCertificationEmail(@Valid @RequestBody UserEmailRequest userEmailRequest) {
        certificationService.sendCertificationNumber(userEmailRequest.getEmailDto());
        return ApiResponse.success(CERTIFICATION_EMAIL_SEND_SUCCESS);
    }

}
