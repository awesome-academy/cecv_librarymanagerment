package com.sun.librarymanagement.domain.entity;

import com.sun.librarymanagement.domain.model.BorrowRequestDetailStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_request_details")
@Getter
@Setter
@NoArgsConstructor
public class BorrowRequestDetailEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrow_request_id", nullable = false)
    private BorrowRequestEntity borrowRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BorrowRequestDetailStatus status;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Builder
    public BorrowRequestDetailEntity(
        BorrowRequestEntity borrowRequest,
        BookEntity book,
        Integer quantity,
        BorrowRequestDetailStatus status
    ) {
        this.borrowRequest = borrowRequest;
        this.book = book;
        this.quantity = quantity;
        this.status = status;
    }
}
