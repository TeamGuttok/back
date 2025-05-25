package com.app.guttokback.user.application.service;

import com.app.guttokback.common.exception.CustomApplicationException;
import com.app.guttokback.common.exception.ErrorCode;
import com.app.guttokback.common.security.Roles;
import com.app.guttokback.email.application.service.ReminderService;
import com.app.guttokback.notification.application.service.NotificationService;
import com.app.guttokback.user.application.dto.serviceDto.UpdateNicknameInfo;
import com.app.guttokback.user.application.dto.serviceDto.UpdatePasswordInfo;
import com.app.guttokback.user.application.dto.serviceDto.UserDetailInfo;
import com.app.guttokback.user.application.dto.serviceDto.UserSaveInfo;
import com.app.guttokback.user.domain.entity.User;
import com.app.guttokback.user.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReminderService reminderService;
    private final NotificationService notificationService;

    @Transactional
    public void userSave(UserSaveInfo userSaveInfo, HttpServletRequest request) {

        // 세션에서 이메일 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            throw new CustomApplicationException(ErrorCode.INVALID_SESSION);
        }

        String sessionEmail = (String) session.getAttribute("email");

        // 세션 이메일과 입력된 이메일이 일치하는지 확인
        if (!sessionEmail.equals(userSaveInfo.getEmail())) {
            throw new CustomApplicationException(ErrorCode.EMAIL_NOT_MATCH);
        }

        isEmailDuplicate(userSaveInfo.getEmail());
        isNickNameDuplicate(userSaveInfo.getNickName());
        User user = userRepository.save(User.builder()
                .role(Roles.ROLE_USER)
                .email(userSaveInfo.getEmail())
                .password(passwordEncoder.encode(userSaveInfo.getPassword()))
                .nickName(userSaveInfo.getNickName())
                .alarm(userSaveInfo.isAlarm())
                .build());
        notificationService.userSaveNotification(user);
    }

    @Transactional
    public void userPasswordUpdate(String email, UpdatePasswordInfo updatePasswordInfo) {
        User user = findByUserEmail(email);
        user.passwordChange(passwordEncoder.encode(updatePasswordInfo.getPassword()));
    }

    @Transactional
    public void userNicknameUpdate(String email, UpdateNicknameInfo updateNicknameInfo) {
        User user = findByUserEmail(email);
        user.nickNameChange(updateNicknameInfo.getNickName());
    }

    @Transactional
    public boolean userAlarmUpdate(String email) {
        User user = findByUserEmail(email);
        user.alarmChange();

        if (user.isAlarm()) {
            reminderService.updateAllRemindersForUser(user.getId());
        }
        return user.isAlarm();
    }

    @Transactional
    public void userDelete(String email) {
        User user = findByUserEmail(email);
        if(user.getAuthorities().contains(Roles.ROLE_USER)) {
            userRepository.delete(user);
        }

    }

    public UserDetailInfo userDetail(String email) {
        User user = findByUserEmail(email);
        return UserDetailInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .alarm(user.isAlarm())
                .build();
    }

    public User userFindById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomApplicationException(ErrorCode.ID_NOT_FOUND));
    }

    public void isEmailDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomApplicationException(ErrorCode.EMAIL_SAME_FOUND);
        }
    }

    public void isNickNameDuplicate(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new CustomApplicationException(ErrorCode.NICKNAME_SAME_FOUND);
        }
    }

    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApplicationException(ErrorCode.EMAIL_NOT_FOUND));
    }
}

