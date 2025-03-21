package com.app.guttokback.user.application.service;

import com.app.guttokback.common.exception.CustomApplicationException;
import com.app.guttokback.common.exception.ErrorCode;
import com.app.guttokback.user.application.dto.serviceDto.GetCertificationNumberInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCertificationNumberService {

    private final RedisTemplate<String, String> redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;

    // 이메일에 해당하는 인증 코드가 맞는지 확인
    private void certification(GetCertificationNumberInfo getCertificationNumberInfo) {
        String email = getCertificationNumberInfo.getEmail();
        String certificationNumber = getCertificationNumberInfo.getCertificationNumber();

        // Redis에서 저장된 인증 코드 가져오기
        String codeKey = "certification:" + email;
        String storedCode = redisTemplate.opsForValue().get(codeKey);

        // 인증 코드가 존재하지 않거나 일치하지 않으면 예외 발생
        if (storedCode == null || !storedCode.equals(certificationNumber)) {
            throw new CustomApplicationException(ErrorCode.CERTIFICATION_NUMBER_NOT_CORRECT);
        }

        redisTemplate.delete(codeKey);

    }

    private HttpSession initializeUserSession(GetCertificationNumberInfo getCertificationNumberInfo, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = request.getSession(true);

        // 세션 만료 시간 설정 (10분 = 600초)
        session.setMaxInactiveInterval(600);

        // 사용자 이메일을 세션에 저장
        String email = getCertificationNumberInfo.getEmail();
        session.setAttribute("email", email);

        return session;
    }


    // 일치하면 로그인과 같은 권한 부여
    public void responseSession(GetCertificationNumberInfo getCertificationNumberInfo, HttpServletRequest request) {
        // 인증 코드 검증
        certification(getCertificationNumberInfo);

        // 세션 초기화 및 생성
        HttpSession session = initializeUserSession(getCertificationNumberInfo, request);

        // 사용자 정보 가져오기
        String email = getCertificationNumberInfo.getEmail();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // 인증 객체 생성 및 SecurityContext 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // 비밀번호를 사용하지 않으므로 null로 설정
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 세션에 Spring Security 컨텍스트 저장
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    // 인증 코드 일치 시 권한 없는 세션 생성 및 반환
    public void createUnauthenticatedSession(GetCertificationNumberInfo getCertificationNumberInfo, HttpServletRequest request) {

        certification(getCertificationNumberInfo);

        initializeUserSession(getCertificationNumberInfo, request);

        // 권한 없이 세션 반환
        SecurityContextHolder.clearContext();
    }


}
