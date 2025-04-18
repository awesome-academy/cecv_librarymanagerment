package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PublishersResponseDto {

    private List<PublisherResponseDto> results;

    private long page;

    @JsonProperty("total_pages")
    private long totalPages;

    @JsonProperty("total_results")
    private long totalResults;
}
