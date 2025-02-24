package com.app.guttokback.user.service;

import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final HttpSession session;

    public void checkSession(HttpSession session) {
        if (session == null) {
            throw new CustomApplicationException(ErrorCode.SESSION_PROBLEM);
        }
    }

    public void checkAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomApplicationException(ErrorCode.INVALID_SESSION);
        }
    }

    public void updateSessionTtl() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) {
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        session.setMaxInactiveInterval(1800);
    }

}
