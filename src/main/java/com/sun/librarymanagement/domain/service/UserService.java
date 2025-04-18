package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.AuthenticationRequestDto;
import com.sun.librarymanagement.domain.dto.request.RegistrationRequestDto;
import com.sun.librarymanagement.domain.dto.response.SuccessResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;

public interface UserService {

    SuccessResponseDto registration(final RegistrationRequestDto user);

    SuccessResponseDto verifyEmail(final String token);

    SuccessResponseDto resendVerification(final String email);

    UserResponseDto authentication(final AuthenticationRequestDto user);
}
