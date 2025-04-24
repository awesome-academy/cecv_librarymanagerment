package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.service.BookService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.BOOKS)
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBook(
        @PathVariable long id,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        BookResponseDto response = bookService.getBook(id, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<BookResponseDto>> getBooks(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        PaginatedResponseDto<BookResponseDto> response = bookService.getBooks(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<BooksResponseDto> search(
        @RequestParam(required = false) String publisher,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String description,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        return ResponseEntity.ok(
            bookService.search(publisher, category, author, name, description, pageNumber, pageSize)
        );
    }

    @PostMapping("{id}/favorite")
    public ResponseEntity<BookResponseDto> favorite(
        @PathVariable long id,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(bookService.favorite(id, currentUser));
    }

    @DeleteMapping("{id}/favorite")
    public ResponseEntity<BookResponseDto> unfavorite(
        @PathVariable long id,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        bookService.unfavorite(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
