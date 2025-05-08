package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.BorrowRequestEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowRequestRepository extends JpaRepository<BorrowRequestEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT request FROM BorrowRequestEntity request WHERE request.id = :id")
    Optional<BorrowRequestEntity> findByIdWithLock(@Param("id") Long id);

    @EntityGraph("fetch-borrower-detailList")
    @Query("SELECT request FROM BorrowRequestEntity request ORDER BY request.createdAt DESC")
    Page<BorrowRequestEntity> findAllWithBorrowerAndDetails(Pageable pageable);

    @EntityGraph("fetch-borrower-detailList")
    @Query("SELECT request FROM BorrowRequestEntity request WHERE request.borrower.id = :id ORDER BY request.createdAt DESC")
    Page<BorrowRequestEntity> findByBorrowerId(@Param("id") Long borrowerId, Pageable pageable);
}
