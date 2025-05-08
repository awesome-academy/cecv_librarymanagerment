package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.RateEntity;
import com.sun.librarymanagement.domain.model.RateInfo;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<RateEntity, RateInfo> {
    @NotNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rate FROM RateEntity rate WHERE rate.id.userId = :userId AND rate.id.bookId = :bookId")
    Optional<RateEntity> findByIdWithLock(
        @NotNull @Param("userId") Long userId,
        @NotNull @Param("bookId") Long bookId
    );

    @NotNull
    @Query("SELECT rate FROM RateEntity rate WHERE rate.id.userId = :userId AND rate.id.bookId = :bookId")
    Optional<RateEntity> findById(
        @NotNull @Param("userId") Long userId,
        @NotNull @Param("bookId") Long bookId
    );
}
