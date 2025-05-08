package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.CreateBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.request.RejectBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.response.BorrowRequestResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.security.AppUserDetails;

public interface BorrowRequestService {

    PaginatedResponseDto<BorrowRequestResponseDto> getBorrowRequests(int pageNumber, int pageSize);

    BorrowRequestResponseDto getBorrowRequest(Long id);

    BorrowRequestResponseDto approveBorrowRequest(Long id);

    BorrowRequestResponseDto rejectBorrowRequest(Long id, RejectBorrowRequestRequestDto borrowRequest);

    BorrowRequestResponseDto borrowBooks(Long borrowRequestId);

    BorrowRequestResponseDto returnBooks(Long borrowRequestId);

    BorrowRequestResponseDto createBorrowRequest(
        CreateBorrowRequestRequestDto borrowRequest,
        AppUserDetails userDetails
    );

    PaginatedResponseDto<BorrowRequestResponseDto> getCurrentUserBorrowRequests(
        int pageNumber,
        int pageSize,
        AppUserDetails userDetails
    );

    BorrowRequestResponseDto getCurrentUserBorrowRequest(Long id, AppUserDetails userDetails);

    BorrowRequestResponseDto cancelBorrowRequest(Long id, AppUserDetails userDetails);
}
