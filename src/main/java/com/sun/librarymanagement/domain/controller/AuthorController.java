package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.service.AuthorService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.AUTHORS)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<AuthorResponseDto>> getAuthors(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        return ResponseEntity.ok(authorService.getAuthors(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthor(
        @PathVariable Long id,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(authorService.getAuthor(id, currentUser));
    }
}
