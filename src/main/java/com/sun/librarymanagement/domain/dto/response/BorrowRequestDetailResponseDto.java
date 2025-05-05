package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.librarymanagement.domain.model.BorrowRequestDetailStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class BorrowRequestDetailResponseDto {

    private Long id;

    private BookResponseDto book;

    private Integer quantity;

    private BorrowRequestDetailStatus status;

    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @JsonProperty("return_date")
    private LocalDate returnDate;
}
