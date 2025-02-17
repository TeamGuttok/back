package com.app.guttokback.user.repository;

import com.app.guttokback.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
    Optional<UserEntity> findByEmail(String email);
}
