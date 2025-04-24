package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BooksResponseDto {
    private List<BookResponseDto> results;

    private long page;

    @JsonProperty("total_pages")
    private long totalPages;

    @JsonProperty("total_results")
    private long totalResults;
}
