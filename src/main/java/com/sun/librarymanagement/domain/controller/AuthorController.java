package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.AuthorsResponseDto;
import com.sun.librarymanagement.domain.service.AuthorService;
import com.sun.librarymanagement.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<AuthorsResponseDto> getAuthors(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        return ResponseEntity.ok(authorService.getAuthors(pageNumber, pageSize));

    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getPublisher(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthor(id));
    }
}
