package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.PublisherEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    boolean existsByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT publisher FROM PublisherEntity publisher WHERE publisher.id = :id")
    Optional<PublisherEntity> findByIdWithLock(@Param("id") Long id);
}
