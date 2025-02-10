package com.app.guttokback.user.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.user.dto.controllerDto.UserNicknameUpdateRequestDto;
import com.app.guttokback.user.dto.controllerDto.UserPasswordUpdateRequestDto;
import com.app.guttokback.user.dto.controllerDto.UserSaveRequestDto;
import com.app.guttokback.user.dto.serviceDto.UserDetailDto;
import com.app.guttokback.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.app.guttokback.global.apiResponse.ResponseMessages.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 상세정보", description = "유저 상세정보 요청")
    @GetMapping
    public ResponseEntity<ApiResponse<UserDetailDto>> userDetail(@AuthenticationPrincipal UserDetails userDetails) {
        UserDetailDto userDetailDto = userService.userDetail(userDetails.getUsername());
        return ApiResponse.success(USER_RETRIEVE_SUCCESS, userDetailDto);
    }

    @Operation(summary = "회원 가입", description = "회원 가입 요청")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<ResponseMessages>> userSave(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto, HttpServletRequest request) {
        userService.userSave(userSaveRequestDto.userSaveDto(), request);
        return ApiResponse.success(USER_SAVE_SUCCESS);
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 요청")
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<ResponseMessages>> userPasswordUpdate(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserPasswordUpdateRequestDto userPasswordUpdateRequestDto) {
        userService.userPasswordUpdate(userDetails.getUsername(), userPasswordUpdateRequestDto.updatePasswordDto());
        return ApiResponse.success(PASSWORD_UPDATE_SUCCESS);
    }

    @Operation(summary = "닉네임 변경", description = "닉네임 변경 요청")
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<ResponseMessages>> userNicknameUpdate(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserNicknameUpdateRequestDto userNicknameUpdateRequestDto) {
        userService.userNicknameUpdate(userDetails.getUsername(), userNicknameUpdateRequestDto.updateNicknameDto());
        return ApiResponse.success(NICKNAME_UPDATE_SUCCESS);
    }

    @Operation(summary = "알림 상태 변경", description = "알림 상태 변경 요청")
    @PatchMapping("/alarm")
    public ResponseEntity<ApiResponse<ResponseMessages>> userAlarmUpdate(@AuthenticationPrincipal UserDetails userDetails) {
        userService.userAlarmUpdate(userDetails.getUsername());
        return ApiResponse.success(ALARM_UPDATE_SUCCESS);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 요청")
    @DeleteMapping
    public ResponseEntity<ApiResponse<ResponseMessages>> userDelete(@AuthenticationPrincipal UserDetails userDetails) {
        userService.userDelete(userDetails.getUsername());
        return ApiResponse.success(USER_DELETE_SUCCESS);
    }
}
