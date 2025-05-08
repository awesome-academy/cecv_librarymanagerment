package com.sun.librarymanagement.domain.entity;

import com.sun.librarymanagement.domain.model.BorrowRequestStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "borrow_requests")
@NamedEntityGraph(
    name = "fetch-borrower-detailList",
    attributeNodes = {@NamedAttributeNode("borrower"), @NamedAttributeNode("detailList")}
)
@Getter
@Setter
@NoArgsConstructor
public class BorrowRequestEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private UserEntity borrower;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BorrowRequestStatus status;

    @Column(name = "status_note", columnDefinition = "TEXT")
    private String statusNote;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "borrowRequest")
    private List<BorrowRequestDetailEntity> detailList;

    @Builder
    public BorrowRequestEntity(
        UserEntity borrower,
        BorrowRequestStatus status,
        String statusNote,
        LocalDate startDate,
        LocalDate endDate
    ) {
        this.borrower = borrower;
        this.status = status;
        this.statusNote = statusNote;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
