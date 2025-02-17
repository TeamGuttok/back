package com.app.guttokback.user.service;

import com.app.guttokback.global.aws.ses.service.ReminderService;
import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import com.app.guttokback.global.security.role.Roles;
import com.app.guttokback.notification.service.NotificationService;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.dto.serviceDto.UpdateNicknameDto;
import com.app.guttokback.user.dto.serviceDto.UpdatePasswordDto;
import com.app.guttokback.user.dto.serviceDto.UserDetailDto;
import com.app.guttokback.user.dto.serviceDto.UserSaveDto;
import com.app.guttokback.user.repository.UserRepository;
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
    public void userSave(UserSaveDto userSaveDto, HttpServletRequest request) {

        // 세션에서 이메일 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            throw new CustomApplicationException(ErrorCode.INVALID_SESSION);
        }

        String sessionEmail = (String) session.getAttribute("email");

        // 세션 이메일과 입력된 이메일이 일치하는지 확인
        if (!sessionEmail.equals(userSaveDto.getEmail())) {
            throw new CustomApplicationException(ErrorCode.EMAIL_NOT_MATCH);
        }

        isEmailDuplicate(userSaveDto.getEmail());
        isNickNameDuplicate(userSaveDto.getNickName());
        UserEntity user = userRepository.save(UserEntity.builder()
                .role(Roles.ROLE_USER)
                .email(userSaveDto.getEmail())
                .password(passwordEncoder.encode(userSaveDto.getPassword()))
                .nickName(userSaveDto.getNickName())
                .alarm(userSaveDto.isAlarm())
                .build());
        notificationService.userSaveNotification(user);
    }

    @Transactional
    public void userPasswordUpdate(String email, UpdatePasswordDto updatePasswordDto) {
        UserEntity userEntity = findByUserEmail(email);
        userEntity.passwordChange(passwordEncoder.encode(updatePasswordDto.getPassword()));
    }

    @Transactional
    public void userNicknameUpdate(String email, UpdateNicknameDto updateNicknameDto) {
        UserEntity userEntity = findByUserEmail(email);
        userEntity.nickNameChange(updateNicknameDto.getNickName());
    }

    @Transactional
    public void userAlarmUpdate(String email) {
        UserEntity userEntity = findByUserEmail(email);
        userEntity.alarmChange();

        if (userEntity.isAlarm()) {
            reminderService.updateAllRemindersForUser(userEntity.getId());
        }
    }

    @Transactional
    public void userDelete(String email) {
        UserEntity userEntity = findByUserEmail(email);
        userRepository.delete(userEntity);
    }

    public UserDetailDto userDetail(String email) {
        UserEntity userEntity = findByUserEmail(email);
        return UserDetailDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .nickName(userEntity.getNickName())
                .alarm(userEntity.isAlarm())
                .build();
    }

    public UserEntity userFindById(Long id) {
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

    public UserEntity findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApplicationException(ErrorCode.EMAIL_NOT_FOUND));
    }
}

