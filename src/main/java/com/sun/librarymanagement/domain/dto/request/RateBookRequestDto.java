package com.sun.librarymanagement.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RateBookRequestDto {
    @JsonProperty("book_id")
    @NotNull
    private Long bookId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rate;
}
