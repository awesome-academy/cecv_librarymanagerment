package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.service.FavoriteService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.BOOKS)
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("{id}/favorite")
    public ResponseEntity<BookResponseDto> favorite(
        @PathVariable long id,
        @NotNull @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(favoriteService.favorite(id, currentUser.getId()));
    }

    @DeleteMapping("{id}/favorite")
    public ResponseEntity<BookResponseDto> unfavorite(
        @PathVariable long id,
        @NotNull @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        favoriteService.unfavorite(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<PaginatedResponseDto<BookResponseDto>> getUserFavoriteBooks(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) @Min(0) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) @Min(1) int pageSize,
        @NotNull @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        PaginatedResponseDto<BookResponseDto> response = favoriteService.getUserFavoriteBooks(pageNumber, pageSize, currentUser.getId());
        return ResponseEntity.ok(response);
    }
}
