package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.ChangeEmailRequestDto;
import com.sun.librarymanagement.domain.dto.request.ChangePasswordRequestDto;
import com.sun.librarymanagement.domain.dto.request.UpdateProfileRequestDto;
import com.sun.librarymanagement.domain.dto.response.BorrowRequestResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.dto.response.SuccessResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.service.BorrowRequestService;
import com.sun.librarymanagement.domain.service.ProfileService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.PROFILES)
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final BorrowRequestService borrowRequestService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUserProfile(
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(profileService.getCurrentUserProfile(userDetails));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateCurrentUserProfile(
        @RequestBody @Valid UpdateProfileRequestDto user,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(profileService.updateCurrentUserProfile(user, userDetails));
    }

    @PatchMapping("/me/email")
    public ResponseEntity<SuccessResponseDto> changeEmail(
        @RequestBody @Valid ChangeEmailRequestDto request,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(profileService.changeEmail(request, userDetails));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<SuccessResponseDto> changePassword(
        @RequestBody @Valid ChangePasswordRequestDto request,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(profileService.changePassword(request, userDetails));
    }

    @GetMapping("/me/borrow-requests")
    public ResponseEntity<PaginatedResponseDto<BorrowRequestResponseDto>> getCurrentUserBorrowRequests(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(borrowRequestService.getCurrentUserBorrowRequests(pageNumber, pageSize, userDetails));
    }
}
