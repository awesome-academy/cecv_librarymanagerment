package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.RateBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.service.RateService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.RATES)
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    @PostMapping
    public ResponseEntity<BookResponseDto> addBookRating(
        @RequestBody @Valid RateBookRequestDto request,
        @NotNull @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        BookResponseDto response = rateService.addBookRating(request, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBookRating(
        @PathVariable long bookId,
        @NotNull @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        rateService.deleteBookRating(currentUser.getId(), bookId);
        return ResponseEntity.noContent().build();
    }
}
