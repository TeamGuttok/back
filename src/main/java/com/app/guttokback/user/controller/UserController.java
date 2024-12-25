package com.app.guttokback.user.controller;

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

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{pk}")
    public ResponseEntity<UserDetailDto> userDetail(@PathVariable Long pk) {
        UserDetailDto userDetailDto = userService.userDetail(pk);
        return ResponseEntity.ok(userDetailDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> userSave(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {
        userService.userSave(userSaveRequestDto.userSaveDto());
        return ResponseEntity.ok("유저 저장 성공");
    }

    @PatchMapping("password/{pk}/{password}")
    public ResponseEntity<String> userPasswordUpdate(@PathVariable Long pk, @PathVariable String password) {
        userService.userPasswordUpdate(pk, password);
        return ResponseEntity.ok("비밀번호 수정 성공");
    }

    @PatchMapping("nickname/{pk}/{nickName}")
    public ResponseEntity<String> userNicknameUpdate(@PathVariable Long pk, @PathVariable String nickName) {
        userService.userNicknameUpdate(pk, nickName);
        return ResponseEntity.ok("닉네임 수정 성공");
    }

    @PatchMapping("alarm/{pk}")
    public ResponseEntity<String> userAlarmdUpdate(@PathVariable Long pk) {
        userService.userAlarmUpdate(pk);
        return ResponseEntity.ok("알림 수정 성공");
    }

    @DeleteMapping("{pk}")
    public ResponseEntity<String> userDelete(@PathVariable Long pk) {
        userService.userDelete(pk);
        return ResponseEntity.ok("유저 삭제 성공");
    }
}
