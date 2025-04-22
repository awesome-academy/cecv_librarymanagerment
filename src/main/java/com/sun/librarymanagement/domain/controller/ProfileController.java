package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.ChangeEmailRequestDto;
import com.sun.librarymanagement.domain.dto.request.ChangePasswordRequestDto;
import com.sun.librarymanagement.domain.dto.request.UpdateProfileRequestDto;
import com.sun.librarymanagement.domain.dto.response.SuccessResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.service.ProfileService;
import com.sun.librarymanagement.security.AppUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

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

    @PostMapping("/me/email")
    public ResponseEntity<SuccessResponseDto> changeEmail(
        @RequestBody @Valid ChangeEmailRequestDto request,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(profileService.changeEmail(request, userDetails));
    }

    @PostMapping("/me/password")
    public ResponseEntity<SuccessResponseDto> changePassword(
        @RequestBody @Valid ChangePasswordRequestDto request,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(profileService.changePassword(request, userDetails));
    }
}
