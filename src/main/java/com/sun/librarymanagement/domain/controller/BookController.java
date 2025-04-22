package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.BookResponse;
import com.sun.librarymanagement.domain.dto.response.BooksResponse;
import com.sun.librarymanagement.domain.service.BookService;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.BOOKS)
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable long id) {
        BookResponse response = bookService.getBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BooksResponse> getBooks(
            @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
            @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        BooksResponse response = bookService.getBooks(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }
}
