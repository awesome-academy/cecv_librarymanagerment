package com.sun.librarymanagement.domain.dto.request;

import lombok.Getter;

@Getter
public class SearchBookRequestDto {

    private String publisher;

    private String category;

    private String author;

    private String name;

    private String description;
}
