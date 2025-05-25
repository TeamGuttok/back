package com.app.guttokback.user.application.service;

import com.app.guttokback.common.security.Roles;
import com.app.guttokback.user.domain.entity.User;
import com.app.guttokback.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestAccountResetService {

    private final UserRepository userRepository;

    @Transactional
    public void resetTestAccounts() {
        List<User> testUsers = userRepository.findTestAccounts(Roles.ROLE_TEST);

        for (User user : testUsers) {
            /*이메일 발송횟수, 구독항목 주기 삭제*/
        }
    }
}
