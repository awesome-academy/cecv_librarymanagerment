package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class AuthorResponseDto {

    private long id;

    private String name;

    private String bio;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("is_following")
    private boolean isFollowing;

    public AuthorResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AuthorResponseDto(long id, String name, String bio, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
    }
}
