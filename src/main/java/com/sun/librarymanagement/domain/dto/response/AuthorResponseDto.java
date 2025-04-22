package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorResponseDto {

    private long id;

    private String name;

    private String bio;

    @JsonProperty("date_of_birth")
    private LocalDateTime dateOfBirth;

    @JsonProperty("is_following")
    private boolean isFollowing;

    public AuthorResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AuthorResponseDto(long id, String name, String bio, LocalDateTime dateOfBirth) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
    }
}
