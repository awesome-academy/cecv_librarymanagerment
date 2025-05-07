package com.sun.librarymanagement.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    private Set<CategoryResponseDto> categories;

    @JsonProperty("is_favorited")
    private boolean isFavorited;
}
