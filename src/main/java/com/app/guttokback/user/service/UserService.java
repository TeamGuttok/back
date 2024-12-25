package com.app.guttokback.user.service;

import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
import com.app.guttokback.global.exception.GlobalExceptionHandler;
import com.app.guttokback.user.domain.UserEntity;
import com.app.guttokback.user.dto.serviceDto.UserDetailDto;
import com.app.guttokback.user.dto.serviceDto.UserSaveDto;
import com.app.guttokback.user.repository.UserRepository;

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

    @Transactional
    public void userSave(UserSaveDto userSaveDto) {
        if (isEmailDuplicate(userSaveDto.getEmail())) {
            throw new CustomApplicationException(ErrorCode.EMAIL_SAME_FOUND);
        }

        userRepository.save(UserEntity.builder()
                .email(userSaveDto.getEmail())
                .password(passwordEncoder.encode(userSaveDto.getPassword()))
                .nickName(userSaveDto.getNickName())
                .alarm(userSaveDto.isAlarm())
                .build());
    }

    @Transactional
    public void userPasswordUpdate(String email, String password) {
        UserEntity userEntity = userFindByEmail(email);
        userEntity.passwordChange(passwordEncoder.encode(password));
    }

    @Transactional
    public void userNicknameUpdate(String email, String nickName) {
        UserEntity userEntity = userFindByEmail(email);
        userEntity.nickNameChange(nickName);
    }

    @Transactional
    public void userAlarmUpdate(String email) {
        UserEntity userEntity = userFindByEmail(email);
        userEntity.alarmChange();
    }

    @Transactional
    public void userDelete(String email) {
        UserEntity userEntity = userFindByEmail(email);
        userRepository.delete(userEntity);
    }

    public UserDetailDto userDetail(String email) {
        UserEntity userEntity = userFindByEmail(email);
        return UserDetailDto.builder()
                .email(userEntity.getEmail())
                .nickName(userEntity.getNickName())
                .alarm(userEntity.isAlarm())
                .build();
    }

    public UserEntity userFindByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApplicationException(ErrorCode.EMAIL_NOT_FOUND));
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}

