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
        isEmailDuplicate(userSaveDto.getEmail());
        userRepository.save(UserEntity.builder()
                .email(userSaveDto.getEmail())
                .password(passwordEncoder.encode(userSaveDto.getPassword()))
                .nickName(userSaveDto.getNickName())
                .alarm(userSaveDto.isAlarm())
                .build());
    }

    @Transactional
    public void userPasswordUpdate(Long id, String password) {
        UserEntity userEntity = userFindById(id);
        userEntity.passwordChange(passwordEncoder.encode(password));
    }

    @Transactional
    public void userNicknameUpdate(Long id, String nickName) {
        UserEntity userEntity = userFindById(id);
        userEntity.nickNameChange(nickName);
    }

    @Transactional
    public void userAlarmUpdate(Long id) {
        UserEntity userEntity = userFindById(id);
        userEntity.alarmChange();
    }

    @Transactional
    public void userDelete(Long id) {
        UserEntity userEntity = userFindById(id);
        userRepository.delete(userEntity);
    }

    public UserDetailDto userDetail(Long id) {
        UserEntity userEntity = userFindById(id);
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
}

