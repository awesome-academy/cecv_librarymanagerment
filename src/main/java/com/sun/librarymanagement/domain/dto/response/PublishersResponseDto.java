package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PublishersResponseDto {

    private List<PublisherResponseDto> results;

    long page;

    @JsonProperty("total_pages")
    long totalPages;

    @JsonProperty("total_results")
    long totalResults;
}
