package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PublisherResponseDto {

    private long id;

    private String name;

    @JsonProperty("is_following")
    private boolean isFollowing;

    public PublisherResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
