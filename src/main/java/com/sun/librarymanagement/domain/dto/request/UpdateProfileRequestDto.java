package com.sun.librarymanagement.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProfileRequestDto {

    @NotBlank(message = "Username is required.")
    private String username;
}
