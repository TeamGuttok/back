package com.app.guttokback.user.service;

import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

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

}
