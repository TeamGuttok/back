package com.app.guttokback.user.controller;


import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.global.apiResponse.ResponseMessages;
import com.app.guttokback.user.dto.controllerDto.LoginRequestDto;
import com.app.guttokback.user.dto.controllerDto.UserCertificationNumberRequestDto;
import com.app.guttokback.user.service.SessionService;
import com.app.guttokback.user.service.UserCertificationNumberService;
import com.app.guttokback.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.app.guttokback.global.apiResponse.ResponseMessages.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserCertificationNumberService userCertificationNumberService;
    private final UserService userService;
    private final SessionService sessionService;

    @Operation(summary = "로그인", description = "로그인 요청")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> signin(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        session = request.getSession(true);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setAttribute("email", loginRequestDto.getEmail());

        String userNickName = userService.userDetail(authentication.getName()).getNickName();

        return ApiResponse.success(USER_LOGIN_SUCCESS, userNickName);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 요청")
    @PostMapping("/signout")
    public ResponseEntity<ApiResponse<ResponseMessages>> signout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        Cookie cookie = new Cookie("SESSION", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ApiResponse.success(USER_LOGOUT_SUCCESS);
    }

    @Operation(summary = "인증번호 검증", description = "인증번호 검증 요청")
    @PostMapping("/certification-number")
    public ResponseEntity<ApiResponse<ResponseMessages>> userCertificationNumber(@Valid @RequestBody UserCertificationNumberRequestDto userCertificationNumberRequestDto, HttpServletRequest request) {
        userCertificationNumberService.responseSession(userCertificationNumberRequestDto.getCertificationNumberDto(), request);
        return ApiResponse.success(CERTIFICATION_NUMBER_SUCCESS);
    }

    @Operation(summary = "이메일 검증", description = "이메일 검증 요청")
    @PostMapping("/email-verification")
    public ResponseEntity<ApiResponse<ResponseMessages>> userEmailVerification(@Valid @RequestBody UserCertificationNumberRequestDto userCertificationNumberRequestDto, HttpServletRequest request) {
        userCertificationNumberService.createUnauthenticatedSession(userCertificationNumberRequestDto.getCertificationNumberDto(), request);
        return ApiResponse.success(EMAIL_VERIFICATIOIN_SUCCESS);
    }

    @Operation(summary = "세션 체크", description = "세션 체크 요청")
    @GetMapping("/check-session")
    public ResponseEntity<ApiResponse<ResponseMessages>> userCheckSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        sessionService.checkSession(session);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        sessionService.checkAuthentication(authentication);

        return ApiResponse.success(SESSION_VALID);
    }
}
