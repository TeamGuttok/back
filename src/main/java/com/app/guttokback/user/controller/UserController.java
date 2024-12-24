package com.app.guttokback.user.controller;

import com.app.guttokback.user.dto.controllerDto.UserSaveRequestDto;
import com.app.guttokback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users/signup")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> userSave(@RequestBody UserSaveRequestDto userSaveRequestDto) {
        userService.userSave(userSaveRequestDto.userSaveDto());
        return ResponseEntity.ok("유저 저장 성공");
    }

    @PatchMapping("password/{email}/{password}")
    public ResponseEntity<String> userPasswordUpdate(@PathVariable String email, @PathVariable String password) {
        userService.userPasswordUpdate(email, password);
        return ResponseEntity.ok("비밀번호 수정 성공");
    }

    @PatchMapping("nickname/{email}/{nickName}")
    public ResponseEntity<String> userNicknameUpdate(@PathVariable String email, @PathVariable String nickName) {
        userService.userNicknameUpdate(email, nickName);
        return ResponseEntity.ok("닉네임 수정 성공");
    }

    @PatchMapping("alarm/{email}")
    public ResponseEntity<String> userAlarmdUpdate(@PathVariable String email) {
        userService.userAlarmdUpdate(email);
        return ResponseEntity.ok("알림 수정 성공");
    }

    @DeleteMapping
    public ResponseEntity<String> userDelete(@PathVariable String email) {
        userService.userDelete(email);
        return ResponseEntity.ok("유저 삭제 성공");
    }
}
