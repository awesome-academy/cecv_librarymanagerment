package com.sun.librarymanagement.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PublisherRequestDto {

    @NotBlank(message = "Publisher name is required.")
    @Size(max = 255, message = "Publisher name must not exceed 255 characters.")
    private String name;
}
