package com.app.guttokback.user.service;

import com.app.guttokback.global.exception.CustomApplicationException;
import com.app.guttokback.global.exception.ErrorCode;
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
    public void userPasswordUpdate(Long pk, String password) {
        UserEntity userEntity = userFindById(pk);
        userEntity.passwordChange(passwordEncoder.encode(password));
    }

    @Transactional
    public void userNicknameUpdate(Long pk, String nickName) {
        UserEntity userEntity = userFindById(pk);
        userEntity.nickNameChange(nickName);
    }

    @Transactional
    public void userAlarmUpdate(Long pk) {
        UserEntity userEntity = userFindById(pk);
        userEntity.alarmChange();
    }

    @Transactional
    public void userDelete(Long pk) {
        UserEntity userEntity = userFindById(pk);
        userRepository.delete(userEntity);
    }

    public UserDetailDto userDetail(Long pk) {
        UserEntity userEntity = userFindById(pk);
        return UserDetailDto.builder()
                .pk(userEntity.getId())
                .email(userEntity.getEmail())
                .nickName(userEntity.getNickName())
                .alarm(userEntity.isAlarm())
                .build();
    }

    public UserEntity userFindById(Long pk) {
        return userRepository.findById(pk)
                .orElseThrow(() -> new CustomApplicationException(ErrorCode.PK_NOT_FOUND));
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}

