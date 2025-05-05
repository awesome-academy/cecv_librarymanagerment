package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.CreateBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.response.BorrowRequestResponseDto;
import com.sun.librarymanagement.domain.service.BorrowRequestService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.BORROW_REQUESTS)
@RequiredArgsConstructor
public class BorrowRequestController {

    private final BorrowRequestService borrowRequestService;

    @PostMapping
    public ResponseEntity<BorrowRequestResponseDto> createBorrowRequest(
        @RequestBody @Valid CreateBorrowRequestRequestDto borrowRequest,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(borrowRequestService.createBorrowRequest(borrowRequest, userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestResponseDto> getBorrowRequest(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.getBorrowRequest(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BorrowRequestResponseDto> cancelBorrowRequest(
        @PathVariable Long id,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(borrowRequestService.cancelBorrowRequest(id, userDetails));
    }
}
