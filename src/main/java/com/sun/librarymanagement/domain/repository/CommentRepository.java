package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.CommentEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT comment FROM CommentEntity comment JOIN comment.bookEntity book WHERE book.id = :book_id")
    Page<CommentEntity> findAllByBookId(@Param("book_id") long id, @NotNull Pageable pageable);
}
