package com.app.guttokback.user.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
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

import static com.app.guttokback.global.apiResponse.ResponseMessages.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDetailDto>> userDetail(@PathVariable Long id) {
        UserDetailDto userDetailDto = userService.userDetail(id);
        return ApiResponse.success(USER_RETRIEVE_SUCCESS, userDetailDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> userSave(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {
        userService.userSave(userSaveRequestDto.userSaveDto());
        return ApiResponse.success(USER_SAVE_SUCCESS);
    }

    @PatchMapping("password/{id}/{password}")
    public ResponseEntity<ApiResponse<Object>> userPasswordUpdate(@PathVariable Long id, @PathVariable String password) {
        userService.userPasswordUpdate(id, password);
        return ApiResponse.success(PASSWORD_UPDATE_SUCCESS);
    }

    @PatchMapping("nickname/{id}/{nickName}")
    public ResponseEntity<ApiResponse<Object>> userNicknameUpdate(@PathVariable Long id, @PathVariable String nickName) {
        userService.userNicknameUpdate(id, nickName);
        return ApiResponse.success(NICKNAME_UPDATE_SUCCESS);
    }

    @PatchMapping("alarm/{id}")
    public ResponseEntity<ApiResponse<Object>> userAlarmUpdate(@PathVariable Long id) {
        userService.userAlarmUpdate(id);
        return ApiResponse.success(ALARM_UPDATE_SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Object>> userDelete(@PathVariable Long id) {
        userService.userDelete(id);
        return ApiResponse.success(USER_DELETE_SUCCESS);
    }
}
