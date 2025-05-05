package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.CreateBorrowRequestDetailRequestDto;
import com.sun.librarymanagement.domain.dto.request.CreateBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.request.RejectBorrowRequestRequestDto;
import com.sun.librarymanagement.domain.dto.response.*;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.BorrowRequestDetailEntity;
import com.sun.librarymanagement.domain.entity.BorrowRequestEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.model.BorrowRequestDetailStatus;
import com.sun.librarymanagement.domain.model.BorrowRequestStatus;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.repository.BorrowRequestRepository;
import com.sun.librarymanagement.domain.repository.UserRepository;
import com.sun.librarymanagement.domain.service.BorrowRequestService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowRequestServiceImpl implements BorrowRequestService {

    private final BorrowRequestRepository borrowRequestRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public BorrowRequestResponseDto createBorrowRequest(
        CreateBorrowRequestRequestDto borrowRequest,
        AppUserDetails userDetails
    ) {
        UserEntity userEntity = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        if (!borrowRequest.getStartDate().isBefore(borrowRequest.getEndDate())) {
            throw new AppException(AppError.BORROW_REQUEST_INVALID);
        }

        BorrowRequestEntity borrowRequestEntity = BorrowRequestEntity.builder()
            .borrower(userEntity)
            .status(BorrowRequestStatus.PENDING)
            .startDate(borrowRequest.getStartDate())
            .endDate(borrowRequest.getEndDate())
            .build();
        List<BorrowRequestDetailEntity> detailList = new ArrayList<>();
        for (CreateBorrowRequestDetailRequestDto detail : borrowRequest.getDetails()) {
            BookEntity bookEntity = bookRepository.findById(detail.getBookId())
                .orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
            detailList.add(
                BorrowRequestDetailEntity.builder()
                    .borrowRequest(borrowRequestEntity)
                    .book(bookEntity)
                    .quantity(detail.getQuantity())
                    .status(BorrowRequestDetailStatus.PENDING)
                    .build()
            );
        }
        borrowRequestEntity.setDetailList(detailList);
        borrowRequestRepository.save(borrowRequestEntity);

        return convertEntityToDto(borrowRequestEntity);
    }

    @Override
    public PaginatedResponseDto<BorrowRequestResponseDto> getBorrowRequests(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<BorrowRequestEntity> page = borrowRequestRepository.findAllWithBorrowerAndDetails(pageable);

        return new PaginatedResponseDto<>(
            convertToBorrowRequestList(page),
            pageNumber,
            page.getTotalPages(),
            page.getTotalElements()
        );
    }

    @Override
    public BorrowRequestResponseDto getBorrowRequest(Long id) {
        BorrowRequestEntity borrowRequestEntity = borrowRequestRepository.findById(id)
            .orElseThrow(() -> new AppException(AppError.BORROW_REQUEST_NOT_FOUND));

        return convertEntityToDto(borrowRequestEntity);
    }

    @Transactional
    @Override
    public BorrowRequestResponseDto cancelBorrowRequest(Long id, AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        BorrowRequestEntity borrowRequestEntity = borrowRequestRepository.findByIdWithLock(id)
            .orElseThrow(() -> new AppException(AppError.BORROW_REQUEST_NOT_FOUND));

        if (!userEntity.getId().equals(borrowRequestEntity.getBorrower().getId()) ||
            borrowRequestEntity.getStatus() != BorrowRequestStatus.PENDING
        ) {
            throw new AppException(AppError.BORROW_REQUEST_CANNOT_BE_CANCELLED);
        }

        borrowRequestEntity.setStatus(BorrowRequestStatus.CANCELLED);
        borrowRequestRepository.save(borrowRequestEntity);

        return convertEntityToDto(borrowRequestEntity);
    }

    @Transactional
    @Override
    public BorrowRequestResponseDto approveBorrowRequest(Long id) {
        BorrowRequestEntity borrowRequestEntity = borrowRequestRepository.findByIdWithLock(id)
            .orElseThrow(() -> new AppException(AppError.BORROW_REQUEST_NOT_FOUND));

        if (borrowRequestEntity.getStatus() != BorrowRequestStatus.PENDING) {
            throw new AppException(AppError.BORROW_REQUEST_CANNOT_BE_APPROVED);
        }

        List<BorrowRequestDetailEntity> detailList = new ArrayList<>(borrowRequestEntity.getDetailList());
        for (BorrowRequestDetailEntity detail : detailList) {
            BookEntity bookEntity = bookRepository.findByIdWithLock(detail.getBook().getId())
                .orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
            if (bookEntity.getAvailableQuantity() < detail.getQuantity()) {
                throw new AppException(AppError.BORROW_REQUEST_CANNOT_BE_APPROVED);
            }
            bookEntity.setAvailableQuantity(bookEntity.getAvailableQuantity() - detail.getQuantity());
            bookRepository.save(bookEntity);
            detail.setStatus(BorrowRequestDetailStatus.AVAILABLE);
        }
        borrowRequestEntity.setDetailList(detailList);
        borrowRequestEntity.setStatus(BorrowRequestStatus.APPROVED);
        borrowRequestRepository.save(borrowRequestEntity);

        return convertEntityToDto(borrowRequestEntity);
    }

    @Transactional
    @Override
    public BorrowRequestResponseDto rejectBorrowRequest(Long id, RejectBorrowRequestRequestDto request) {
        BorrowRequestEntity borrowRequestEntity = borrowRequestRepository.findByIdWithLock(id)
            .orElseThrow(() -> new AppException(AppError.BORROW_REQUEST_NOT_FOUND));

        if (borrowRequestEntity.getStatus() != BorrowRequestStatus.PENDING) {
            throw new AppException(AppError.BORROW_REQUEST_CANNOT_BE_REJECTED);
        } else if (request.getReason().isBlank()) {
            throw new AppException(AppError.BORROW_REQUEST_CANNOT_BE_REJECTED_WITHOUT_A_REASON);
        }

        borrowRequestEntity.setStatus(BorrowRequestStatus.REJECTED);
        borrowRequestEntity.setStatusNote(request.getReason());
        borrowRequestRepository.save(borrowRequestEntity);

        return convertEntityToDto(borrowRequestEntity);
    }

    @Transactional
    @Override
    public BorrowRequestResponseDto borrowBooks(Long borrowRequestId) {
        BorrowRequestEntity borrowRequestEntity = borrowRequestRepository.findByIdWithLock(borrowRequestId)
            .orElseThrow(() -> new AppException(AppError.BORROW_REQUEST_NOT_FOUND));

        if (borrowRequestEntity.getStatus() != BorrowRequestStatus.APPROVED) {
            throw new AppException(AppError.BORROW_REQUEST_NOT_APPROVED);
        }

        List<BorrowRequestDetailEntity> detailList = new ArrayList<>(borrowRequestEntity.getDetailList());
        for (BorrowRequestDetailEntity detail : detailList) {
            if (detail.getStatus() != BorrowRequestDetailStatus.AVAILABLE) {
                throw new AppException(AppError.BORROW_REQUEST_NOT_APPROVED);
            }
            detail.setBorrowDate(LocalDate.now());
            detail.setStatus(BorrowRequestDetailStatus.BORROWED);
        }
        borrowRequestEntity.setDetailList(detailList);
        borrowRequestRepository.save(borrowRequestEntity);

        return convertEntityToDto(borrowRequestEntity);
    }

    @Transactional
    @Override
    public BorrowRequestResponseDto returnBooks(Long borrowRequestId) {
        BorrowRequestEntity borrowRequestEntity = borrowRequestRepository.findByIdWithLock(borrowRequestId)
            .orElseThrow(() -> new AppException(AppError.BORROW_REQUEST_NOT_FOUND));

        if (borrowRequestEntity.getStatus() != BorrowRequestStatus.APPROVED) {
            throw new AppException(AppError.BORROW_REQUEST_NOT_APPROVED);
        }

        List<BorrowRequestDetailEntity> detailList = new ArrayList<>(borrowRequestEntity.getDetailList());
        for (BorrowRequestDetailEntity detail : detailList) {
            if (detail.getStatus() != BorrowRequestDetailStatus.BORROWED &&
                detail.getStatus() != BorrowRequestDetailStatus.OVERDUE
            ) {
                throw new AppException(AppError.BORROW_REQUEST_CANNOT_BE_COMPLETED);
            }
            BookEntity bookEntity = bookRepository.findByIdWithLock(detail.getBook().getId())
                .orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
            bookEntity.setAvailableQuantity(bookEntity.getAvailableQuantity() + detail.getQuantity());
            bookRepository.save(bookEntity);
            detail.setReturnDate(LocalDate.now());
            detail.setStatus(BorrowRequestDetailStatus.RETURNED);
        }
        borrowRequestEntity.setDetailList(detailList);
        borrowRequestEntity.setStatus(BorrowRequestStatus.COMPLETED);
        borrowRequestRepository.save(borrowRequestEntity);

        return convertEntityToDto(borrowRequestEntity);
    }

    @Override
    public PaginatedResponseDto<BorrowRequestResponseDto> getCurrentUserBorrowRequests(
        int pageNumber,
        int pageSize,
        AppUserDetails userDetails
    ) {
        UserEntity userEntity = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<BorrowRequestEntity> page = borrowRequestRepository.findByBorrowerId(userEntity.getId(), pageable);

        return new PaginatedResponseDto<>(
            convertToBorrowRequestList(page),
            pageNumber,
            page.getTotalPages(),
            page.getTotalElements()
        );
    }

    private BorrowRequestResponseDto convertEntityToDto(BorrowRequestEntity entity) {
        return BorrowRequestResponseDto.builder()
            .id(entity.getId())
            .borrower(convertEntityToDto(entity.getBorrower()))
            .status(entity.getStatus())
            .statusNote(entity.getStatusNote())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .details(convertToBorrowRequestDetailList(entity.getDetailList()))
            .build();
    }

    private BorrowRequestDetailResponseDto convertEntityToDto(BorrowRequestDetailEntity entity) {
        return BorrowRequestDetailResponseDto.builder()
            .id(entity.getId())
            .book(convertEntityToDto(entity.getBook()))
            .quantity(entity.getQuantity())
            .status(entity.getStatus())
            .borrowDate(entity.getBorrowDate())
            .returnDate(entity.getReturnDate())
            .build();
    }

    private UserResponseDto convertEntityToDto(UserEntity entity) {
        return UserResponseDto.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .build();
    }

    private BookResponseDto convertEntityToDto(BookEntity entity) {
        return BookResponseDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .build();
    }

    private List<BorrowRequestResponseDto> convertToBorrowRequestList(
        Page<BorrowRequestEntity> borrowRequestEntities
    ) {
        return borrowRequestEntities.stream()
            .map(this::convertEntityToDto)
            .collect(Collectors.toList());
    }

    private List<BorrowRequestDetailResponseDto> convertToBorrowRequestDetailList(
        List<BorrowRequestDetailEntity> borrowRequestDetailEntities
    ) {
        return borrowRequestDetailEntities.stream()
            .map(this::convertEntityToDto)
            .collect(Collectors.toList());
    }
}
