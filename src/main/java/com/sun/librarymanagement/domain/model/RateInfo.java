package com.sun.librarymanagement.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateInfo {

    @JoinColumn(name = "user_id")
    private Long userId;

    @JoinColumn(name = "book_id")
    private Long bookId;
}
