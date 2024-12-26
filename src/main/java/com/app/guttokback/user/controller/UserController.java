package com.app.guttokback.user.controller;

import com.app.guttokback.global.response.ApiResponse;
import com.app.guttokback.user.dto.controllerDto.UserSaveRequestDto;
import com.app.guttokback.user.dto.serviceDto.UserDetailDto;
import com.app.guttokback.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.app.guttokback.global.response.ResponseMessages.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{pk}")
    public ResponseEntity<ApiResponse<UserDetailDto>> userDetail(@PathVariable Long pk) {
        UserDetailDto userDetailDto = userService.userDetail(pk);
        return ApiResponse.success(USER_RETRIEVE_SUCCESS, userDetailDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> userSave(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {
        userService.userSave(userSaveRequestDto.userSaveDto());
        return ApiResponse.success(USER_SAVE_SUCCESS);
    }

    @PatchMapping("password/{pk}/{password}")
    public ResponseEntity<ApiResponse<Object>> userPasswordUpdate(@PathVariable Long pk, @PathVariable String password) {
        userService.userPasswordUpdate(pk, password);
        return ApiResponse.success(PASSWORD_UPDATE_SUCCESS);
    }

    @PatchMapping("nickname/{pk}/{nickName}")
    public ResponseEntity<ApiResponse<Object>> userNicknameUpdate(@PathVariable Long pk, @PathVariable String nickName) {
        userService.userNicknameUpdate(pk, nickName);
        return ApiResponse.success(NICKNAME_UPDATE_SUCCESS);
    }

    @PatchMapping("alarm/{pk}")
    public ResponseEntity<ApiResponse<Object>> userAlarmUpdate(@PathVariable Long pk) {
        userService.userAlarmUpdate(pk);
        return ApiResponse.success(ALARM_UPDATE_SUCCESS);
    }

    @DeleteMapping("{pk}")
    public ResponseEntity<ApiResponse<Object>> userDelete(@PathVariable Long pk) {
        userService.userDelete(pk);
        return ApiResponse.success(USER_DELETE_SUCCESS);
    }
}
