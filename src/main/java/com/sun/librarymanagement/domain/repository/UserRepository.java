package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.model.UserRole;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByVerifyToken(String token);

    List<UserEntity> findByUsernameOrEmail(String username, String email);

    Page<UserEntity> findByRole(UserRole role, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT user FROM UserEntity user WHERE user.id = :id")
    Optional<UserEntity> findByIdWithLock(@Param("id") Long id);
}
