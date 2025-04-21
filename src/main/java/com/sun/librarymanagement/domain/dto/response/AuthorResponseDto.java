package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorResponseDto {

    private long id;

    private String name;

    private String bio;

    private LocalDateTime dateOfBirth;

    public AuthorResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
