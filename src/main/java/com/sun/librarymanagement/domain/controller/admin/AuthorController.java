package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.AuthorRequestDto;
import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminAuthorController")
@RequestMapping("/api/v1/admin/authors")
@AllArgsConstructor()
public class AuthorController extends AdminController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDto> addAuthor(
        @RequestBody @Valid AuthorRequestDto author
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(authorService.addAuthor(author));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(
        @PathVariable Long id,
        @Valid @RequestBody AuthorRequestDto author
    ) {
        return ResponseEntity.ok(authorService.updateAuthor(id, author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
