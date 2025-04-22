package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class UserResponseDto {

    private String token;

    private String username;

    private String email;

    private Boolean isActive;

    private Boolean isVerified;

    @Getter
    @Builder
    public static class Multiple {
        private List<UserResponseDto> results;

        private long page;

        @JsonProperty("total_pages")
        private long totalPages;

        @JsonProperty("total_results")
        private long totalResults;
    }
}
