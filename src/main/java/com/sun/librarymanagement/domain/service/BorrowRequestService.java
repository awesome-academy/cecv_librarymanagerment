package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.CreateBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.request.RejectBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.response.BorrowRequestResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.security.AppUserDetails;
import jakarta.mail.MessagingException;

public interface BorrowRequestService {

    BorrowRequestResponseDto createBorrowRequest(
        CreateBorrowRequestRequestDto borrowRequest,
        AppUserDetails userDetails
    );

    PaginatedResponseDto<BorrowRequestResponseDto> getBorrowRequests(int pageNumber, int pageSize);

    BorrowRequestResponseDto getBorrowRequest(Long id);

    BorrowRequestResponseDto cancelBorrowRequest(Long id, AppUserDetails userDetails);

    BorrowRequestResponseDto approveBorrowRequest(Long id) throws MessagingException;

    BorrowRequestResponseDto rejectBorrowRequest(
        Long id,
        RejectBorrowRequestRequestDto borrowRequest
    ) throws MessagingException;

    BorrowRequestResponseDto borrowBooks(Long borrowRequestId);

    BorrowRequestResponseDto returnBooks(Long borrowRequestId);

    PaginatedResponseDto<BorrowRequestResponseDto> getCurrentUserBorrowRequests(
        int pageNumber,
        int pageSize,
        AppUserDetails userDetails
    );
}
