package com.sun.librarymanagement.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "Body is required.")
    private String body;
}
