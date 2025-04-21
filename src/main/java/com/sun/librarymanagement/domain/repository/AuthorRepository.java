package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.AuthorEntity;
import com.sun.librarymanagement.domain.entity.PublisherEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    boolean existsByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT authorEntity FROM AuthorEntity authorEntity WHERE authorEntity.id = :id")
    Optional<AuthorEntity> findByIdWithLock(@Param("id") Long id);
}
