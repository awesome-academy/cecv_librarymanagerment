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

import java.util.List;
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

    @Query("""
            SELECT book FROM BookEntity book
            JOIN book.categories category
            JOIN book.publisher publisher
            JOIN book.author author
            WHERE (:publisher IS NULL OR LOWER(publisher.name) LIKE LOWER(CONCAT('%', :publisher, '%')))
                AND (:category IS NULL OR LOWER(category.name) LIKE LOWER(CONCAT('%', :category, '%')))
                AND (:author IS NULL OR LOWER(author.name) LIKE LOWER(CONCAT('%', :author, '%')))
                AND (:name IS NULL OR LOWER(book.name) LIKE LOWER(CONCAT('%', :name, '%')))
                AND (:description IS NULL OR LOWER(book.description) LIKE LOWER(CONCAT('%', :description, '%')))
                AND book.deletedAt IS NULL
        """)
    Page<BookEntity> searchBook(
        @Param("publisher") String publisher,
        @Param("category") String category,
        @Param("author") String author,
        @Param("name") String name,
        @Param("description") String description,
        Pageable pageable
    );

    @Query("""
            SELECT book FROM BookEntity book
            JOIN book.categories category
            JOIN book.publisher publisher
            JOIN book.author author
            WHERE (:publisher IS NULL OR LOWER(publisher.name) LIKE LOWER(CONCAT('%', :publisher, '%')))
                AND (:category IS NULL OR LOWER(category.name) LIKE LOWER(CONCAT('%', :category, '%')))
                AND (:author IS NULL OR LOWER(author.name) LIKE LOWER(CONCAT('%', :author, '%')))
                AND (:name IS NULL OR LOWER(book.name) LIKE LOWER(CONCAT('%', :name, '%')))
                AND (:description IS NULL OR LOWER(book.description) LIKE LOWER(CONCAT('%', :description, '%')))
                AND book.deletedAt IS NULL
        """)
    List<BookEntity> searchBook(
        @Param("publisher") String publisher,
        @Param("category") String category,
        @Param("author") String author,
        @Param("name") String name,
        @Param("description") String description
    );

    @Query("""
            SELECT book FROM BookEntity book
            LEFT JOIN book.favorites user
            WHERE user.id = :userId
                AND book.deletedAt IS NULL
        """)
    Page<BookEntity> getFavoriteBooks(
            long userId,
            Pageable pageable
    );
}
