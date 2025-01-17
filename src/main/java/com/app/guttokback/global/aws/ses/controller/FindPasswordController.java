package com.app.guttokback.global.aws.ses.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.aws.ses.service.FindPasswordService;
import com.app.guttokback.global.aws.ses.dto.controllerDto.UserEmailRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.app.guttokback.global.apiResponse.ResponseMessages.CERTIFICATION_EMAIL_SEND_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class FindPasswordController {

    private final FindPasswordService findPasswordService;

    @PostMapping("/find-password")
    public ResponseEntity<ApiResponse<Object>> userFindPassword(@Valid @RequestBody UserEmailRequestDto userEmailRequestDto) {
        findPasswordService.sendCertificationNumber(userEmailRequestDto.getEmailDto());
        return ApiResponse.success(CERTIFICATION_EMAIL_SEND_SUCCESS);
    }
}
