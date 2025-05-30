package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PaginatedResponseDto<T> {

    private List<T> results;

    private long page;

    @JsonProperty("total_pages")
    private long totalPages;

    @JsonProperty("total_results")
    private long totalResults;
}
