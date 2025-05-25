package com.app.guttokback.user.application.controller;

import com.app.guttokback.common.api.ApiResponse;
import com.app.guttokback.common.api.ResponseMessages;
import com.app.guttokback.user.application.dto.controllerDto.UserNicknameUpdateRequest;
import com.app.guttokback.user.application.dto.controllerDto.UserPasswordUpdateRequest;
import com.app.guttokback.user.application.dto.controllerDto.UserSaveRequest;
import com.app.guttokback.user.application.dto.serviceDto.UserDetailInfo;
import com.app.guttokback.user.application.service.SessionService;
import com.app.guttokback.user.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.app.guttokback.common.api.ResponseMessages.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    @Operation(summary = "유저 상세정보", description = "유저 상세정보 요청")
    @GetMapping
    public ResponseEntity<ApiResponse<UserDetailInfo>> userDetail(@AuthenticationPrincipal UserDetails userDetails) {
        UserDetailInfo userDetailInfo = userService.userDetail(userDetails.getUsername());
        sessionService.updateSessionTtl();
        return ApiResponse.success(USER_RETRIEVE_SUCCESS, userDetailInfo);
    }

    @Operation(summary = "회원 가입", description = "회원 가입 요청")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<ResponseMessages>> userSave(
            @Valid @RequestBody UserSaveRequest userSaveRequest,
            HttpServletRequest request
    ) {
        userService.userSave(userSaveRequest.userSaveDto(), request);
        return ApiResponse.success(USER_SAVE_SUCCESS);
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 요청")
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<ResponseMessages>> userPasswordUpdate(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest
    ) {
        userService.userPasswordUpdate(userDetails.getUsername(), userPasswordUpdateRequest.updatePasswordDto());
        return ApiResponse.success(PASSWORD_UPDATE_SUCCESS);
    }

    @Operation(summary = "닉네임 변경", description = "닉네임 변경 요청")
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<ResponseMessages>> userNicknameUpdate(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserNicknameUpdateRequest userNicknameUpdateRequest
    ) {
        userService.userNicknameUpdate(userDetails.getUsername(), userNicknameUpdateRequest.updateNicknameDto());
        sessionService.updateSessionTtl();
        return ApiResponse.success(NICKNAME_UPDATE_SUCCESS);
    }

    @Operation(summary = "알림 상태 변경", description = "알림 상태 변경 요청")
    @PatchMapping("/alarm")
    public ResponseEntity<ApiResponse<Boolean>> userAlarmUpdate(@AuthenticationPrincipal UserDetails userDetails) {
        boolean userAlarm = userService.userAlarmUpdate(userDetails.getUsername());
        sessionService.updateSessionTtl();
        return ApiResponse.success(ALARM_UPDATE_SUCCESS, userAlarm);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 요청")
    @DeleteMapping
    public ResponseEntity<ApiResponse<ResponseMessages>> userDelete(@AuthenticationPrincipal UserDetails userDetails,
                                                                    HttpServletRequest request,
                                                                    HttpServletResponse response) {

        boolean status = userService.userDelete(userDetails.getUsername());

        if(status) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("SESSION".equals(cookie.getName())) {
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
            }
        }

        return ApiResponse.success(USER_DELETE_SUCCESS);
    }

}
