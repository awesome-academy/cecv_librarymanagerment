package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.BookEntity;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    @NotNull
    @Query("SELECT book FROM BookEntity book WHERE book.id = :id AND book.deletedAt IS NULL")
    Optional<BookEntity> findById(@NotNull @Param("id") Long id);

    @NotNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT book FROM BookEntity book WHERE book.id = :id AND book.deletedAt IS NULL")
    Optional<BookEntity> findByIdWithLock(@NotNull @Param("id") Long id);

    @NotNull
    @Query("SELECT book FROM BookEntity book WHERE book.deletedAt IS NULL")
    Page<BookEntity> findAll(@NotNull Pageable pageable);
}
