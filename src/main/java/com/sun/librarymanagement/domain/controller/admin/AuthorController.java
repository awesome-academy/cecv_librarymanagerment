package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.AuthorRequestDto;
import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.service.AuthorService;
import com.sun.librarymanagement.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController("adminAuthorController")
@RequestMapping(ApiPaths.AUTHORS_ADMIN)
@AllArgsConstructor()
public class AuthorController extends AdminController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDto> addAuthor(
        @RequestBody @Valid AuthorRequestDto author
    ) {
        AuthorResponseDto response = authorService.addAuthor(author);
        URI location = URI.create(ApiPaths.AUTHORS + "/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(
        @PathVariable Long id,
        @Valid @RequestBody AuthorRequestDto author
    ) {
        return ResponseEntity.ok(authorService.updateAuthor(id, author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
