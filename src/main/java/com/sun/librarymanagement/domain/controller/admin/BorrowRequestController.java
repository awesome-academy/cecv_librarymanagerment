package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.RejectBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.response.BorrowRequestResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.service.BorrowRequestService;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminBorrowRequestController")
@RequestMapping(ApiPaths.BORROW_REQUESTS_ADMIN)
@RequiredArgsConstructor
public class BorrowRequestController extends AdminController {

    private final BorrowRequestService borrowRequestService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<BorrowRequestResponseDto>> getBorrowRequests(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        return ResponseEntity.ok(borrowRequestService.getBorrowRequests(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestResponseDto> getBorrowRequest(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.getBorrowRequest(id));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<BorrowRequestResponseDto> approveBorrowRequest(
        @PathVariable Long id
    ) throws MessagingException {
        return ResponseEntity.ok(borrowRequestService.approveBorrowRequest(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<BorrowRequestResponseDto> rejectBorrowRequest(
        @PathVariable Long id,
        @RequestBody @Valid RejectBorrowRequestRequestDto request
    ) throws MessagingException {
        return ResponseEntity.ok(borrowRequestService.rejectBorrowRequest(id, request));
    }

    @PatchMapping("/{id}/borrow")
    public ResponseEntity<BorrowRequestResponseDto> borrowBooks(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.borrowBooks(id));
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<BorrowRequestResponseDto> returnBooks(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.returnBooks(id));
    }
}
