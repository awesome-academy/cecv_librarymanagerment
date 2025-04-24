package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BookResponseDto {
    private long id;

    private String name;

    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    private PublisherResponseDto publisher;

    private int quantity;

    @JsonProperty("available_quantity")
    private int availableQuantity;

    private AuthorResponseDto author;

    private Set<CategoryResponse> categories;
}
