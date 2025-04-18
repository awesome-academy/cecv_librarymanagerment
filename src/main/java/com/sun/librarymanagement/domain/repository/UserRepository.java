package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByVerifyToken(String token);

    List<UserEntity> findByUsernameOrEmail(String username, String email);
}
