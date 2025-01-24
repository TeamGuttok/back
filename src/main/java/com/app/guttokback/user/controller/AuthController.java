package com.app.guttokback.user.controller;


import com.app.guttokback.global.apiResponse.ApiResponse;
import com.app.guttokback.user.dto.controllerDto.LoginRequestDto;
import com.app.guttokback.user.dto.controllerDto.UserCertificationNumberRequestDto;
import com.app.guttokback.user.service.UserCertificationNumberService;
import com.app.guttokback.user.service.UserService;
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

    @PostMapping("/signout")
    public ResponseEntity<ApiResponse<Object>> signout(HttpServletRequest request, HttpServletResponse response) {
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

    @PostMapping("/certification-number")
    public ResponseEntity<ApiResponse<Object>> userCertificationNumber(@Valid @RequestBody UserCertificationNumberRequestDto userCertificationNumberRequestDto, HttpServletRequest request) {
        userCertificationNumberService.responseSession(userCertificationNumberRequestDto.getCertificationNumberDto(), request);
        return ApiResponse.success(CERTIFICATION_NUMBER_SUCCESS);
    }

    @PostMapping("/email-verification")
    public ResponseEntity<ApiResponse<Object>> userEmailVerification(@Valid @RequestBody UserCertificationNumberRequestDto userCertificationNumberRequestDto, HttpServletRequest request) {
        userCertificationNumberService.createUnauthenticatedSession(userCertificationNumberRequestDto.getCertificationNumberDto(), request);
        return ApiResponse.success(EMAIL_VERIFICATIOIN_SUCCESS);
    }
}
