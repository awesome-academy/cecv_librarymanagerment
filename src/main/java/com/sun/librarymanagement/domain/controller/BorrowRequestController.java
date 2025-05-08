package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.CreateBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.response.BorrowRequestResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.service.BorrowRequestService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
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
    public ResponseEntity<BorrowRequestResponseDto> create(
        @RequestBody @Valid CreateBorrowRequestRequestDto borrowRequest,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(borrowRequestService.createBorrowRequest(borrowRequest, userDetails));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<BorrowRequestResponseDto>> index(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(borrowRequestService.getCurrentUserBorrowRequests(pageNumber, pageSize, userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestResponseDto> show(
        @PathVariable Long id,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(borrowRequestService.getCurrentUserBorrowRequest(id, userDetails));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BorrowRequestResponseDto> cancel(
        @PathVariable Long id,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(borrowRequestService.cancelBorrowRequest(id, userDetails));
    }
}
