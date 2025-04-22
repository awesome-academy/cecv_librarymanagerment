package com.sun.librarymanagement.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryRequest {
    @NotBlank(message = "Category name is required.")
    @Size(max = 255, message = "Category name must not exceed 255 characters.")
    private String name;
}
