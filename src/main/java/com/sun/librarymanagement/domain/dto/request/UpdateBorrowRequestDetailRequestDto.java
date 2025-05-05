package com.sun.librarymanagement.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.librarymanagement.domain.model.BorrowRequestDetailStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateBorrowRequestDetailRequestDto {

    private Long id;

    private BorrowRequestDetailStatus status;

    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @JsonProperty("return_date")
    private LocalDate returnDate;
}
