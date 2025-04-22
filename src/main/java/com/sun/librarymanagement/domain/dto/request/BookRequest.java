package com.sun.librarymanagement.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Set;

@Getter
public class BookRequest {
    @NotBlank(message = "Book name is required.")
    @Size(max = 255, message = "Book name must not exceed 255 characters.")
    private String name;

    private String description;

    @JsonProperty("image_url")
    @Size(max = 255, message = "Book image URL must not exceed 255 characters.")
    private String imageUrl;

    @NotNull
    @Positive(message = "Book quantity must be positive.")
    private Integer quantity;

    @JsonProperty("publisher_id")
    @NotNull
    private Long publisherId;

    @JsonProperty("author_id")
    @NotNull
    private Long authorId;

    @JsonProperty("category_ids")
    @NotNull
    private Set<Long> categoryIds;
}
