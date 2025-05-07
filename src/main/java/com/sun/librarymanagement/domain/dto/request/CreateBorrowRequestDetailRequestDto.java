package com.sun.librarymanagement.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CreateBorrowRequestDetailRequestDto {

    @JsonProperty("book_id")
    private Long bookId;

    @Positive
    private Integer quantity;
}
