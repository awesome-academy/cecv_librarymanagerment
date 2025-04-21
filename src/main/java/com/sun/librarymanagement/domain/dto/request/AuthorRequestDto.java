package com.sun.librarymanagement.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthorRequestDto {

    @NotBlank(message = "Author name is required.")
    @Size(max = 255, message = "Author name must not exceed 255 characters.")
    private String name;

    private String bio;

    private LocalDateTime dateOfBirth;
}
