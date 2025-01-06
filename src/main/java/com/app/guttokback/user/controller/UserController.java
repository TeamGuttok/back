package com.app.guttokback.user.controller;

import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import com.app.guttokback.user.dto.controllerDto.UserSaveRequestDto;
import com.app.guttokback.user.dto.serviceDto.UserDetailDto;
import com.app.guttokback.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.app.guttokback.global.apiResponse.ResponseMessages.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    private String getSessionEmail(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            throw new CustomApplicationException(ErrorCode.SESSION_PROBLEM);
        }
        return (String) session.getAttribute("email");
    }

    private void validateUserAccess(Long userId, HttpServletRequest request) {
        String sessionEmail = getSessionEmail(request);
        UserDetailDto userDetailDto = userService.userDetail(userId);
        if (!userDetailDto.getEmail().equals(sessionEmail)) {
            throw new CustomApplicationException(ErrorCode.USER_ACCESS_PROBLEM);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDetailDto>> userDetail(@PathVariable Long id, HttpServletRequest request) {
        validateUserAccess(id, request);
        UserDetailDto userDetailDto = userService.userDetail(id);
        return ApiResponse.success(USER_RETRIEVE_SUCCESS, userDetailDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> userSave(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {
        userService.userSave(userSaveRequestDto.userSaveDto());
        return ApiResponse.success(USER_SAVE_SUCCESS);
    }

    @PatchMapping("password/{id}/{password}")
    public ResponseEntity<ApiResponse<Object>> userPasswordUpdate(@PathVariable Long id, @PathVariable String password, HttpServletRequest request) {
        validateUserAccess(id, request);
        userService.userPasswordUpdate(id, password);
        return ApiResponse.success(PASSWORD_UPDATE_SUCCESS);
    }

    @PatchMapping("nickname/{id}/{nickName}")
    public ResponseEntity<ApiResponse<Object>> userNicknameUpdate(@PathVariable Long id, @PathVariable String nickName, HttpServletRequest request) {
        validateUserAccess(id, request);
        userService.userNicknameUpdate(id, nickName);
        return ApiResponse.success(NICKNAME_UPDATE_SUCCESS);
    }

    @PatchMapping("alarm/{id}")
    public ResponseEntity<ApiResponse<Object>> userAlarmUpdate(@PathVariable Long id, HttpServletRequest request) {
        validateUserAccess(id, request);
        userService.userAlarmUpdate(id);
        return ApiResponse.success(ALARM_UPDATE_SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Object>> userDelete(@PathVariable Long id, HttpServletRequest request) {
        validateUserAccess(id, request);
        userService.userDelete(id);
        return ApiResponse.success(USER_DELETE_SUCCESS);
    }
}
