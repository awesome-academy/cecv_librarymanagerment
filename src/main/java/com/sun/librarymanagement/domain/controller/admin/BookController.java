package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.BookRequest;
import com.sun.librarymanagement.domain.dto.response.BookResponse;
import com.sun.librarymanagement.domain.service.BookService;
import com.sun.librarymanagement.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController("adminBookController")
@RequestMapping(ApiPaths.BOOKS_ADMIN)
@AllArgsConstructor()
public class BookController extends AdminController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@RequestBody @Valid BookRequest request) {
        BookResponse response = bookService.addBook(request);
        URI location = URI.create(ApiPaths.BOOKS + "/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable long id, @RequestBody @Valid BookRequest request) {
        BookResponse response = bookService.updateBook(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
