package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.ChangeEmailRequestDto;
import com.sun.librarymanagement.domain.dto.request.ChangePasswordRequestDto;
import com.sun.librarymanagement.domain.dto.request.UpdateProfileRequestDto;
import com.sun.librarymanagement.domain.dto.response.SuccessResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.security.AppUserDetails;

public interface ProfileService {

    UserResponseDto getCurrentUserProfile(final AppUserDetails userDetails);

    UserResponseDto updateCurrentUserProfile(
        final UpdateProfileRequestDto user,
        final AppUserDetails userDetails
    );

    SuccessResponseDto changeEmail(
        final ChangeEmailRequestDto request,
        final AppUserDetails userDetails
    );

    SuccessResponseDto changePassword(
        final ChangePasswordRequestDto request,
        final AppUserDetails userDetails
    );
}
