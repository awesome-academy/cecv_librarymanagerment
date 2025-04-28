package com.sun.librarymanagement.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchBookRequestDto {

    private String publisher;

    private String category;

    private String author;

    private String name;

    private String description;
}
