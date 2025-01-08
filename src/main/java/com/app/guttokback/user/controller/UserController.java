package com.app.guttokback.user.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.user.dto.controllerDto.UserSaveRequestDto;
import com.app.guttokback.user.dto.serviceDto.UserDetailDto;
import com.app.guttokback.user.service.UserService;
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

    @GetMapping
    public ResponseEntity<ApiResponse<UserDetailDto>> userDetail(@AuthenticationPrincipal UserDetails userDetails) {
        UserDetailDto userDetailDto = userService.userDetail(userDetails.getUsername());
        return ApiResponse.success(USER_RETRIEVE_SUCCESS, userDetailDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> userSave(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {
        userService.userSave(userSaveRequestDto.userSaveDto());
        return ApiResponse.success(USER_SAVE_SUCCESS);
    }

    @PatchMapping("password/{password}")
    public ResponseEntity<ApiResponse<Object>> userPasswordUpdate(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String password, HttpServletRequest request) {
        userService.userPasswordUpdate(userDetails.getUsername(), password);
        return ApiResponse.success(PASSWORD_UPDATE_SUCCESS);
    }

    @PatchMapping("nickname/{nickName}")
    public ResponseEntity<ApiResponse<Object>> userNicknameUpdate(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String nickName, HttpServletRequest request) {
        userService.userNicknameUpdate(userDetails.getUsername(), nickName);
        return ApiResponse.success(NICKNAME_UPDATE_SUCCESS);
    }

    @PatchMapping("alarm")
    public ResponseEntity<ApiResponse<Object>> userAlarmUpdate(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        userService.userAlarmUpdate(userDetails.getUsername());
        return ApiResponse.success(ALARM_UPDATE_SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> userDelete(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        userService.userDelete(userDetails.getUsername());
        return ApiResponse.success(USER_DELETE_SUCCESS);
    }
}
