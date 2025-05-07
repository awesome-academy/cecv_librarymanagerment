package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.AuthenticationRequestDto;
import com.sun.librarymanagement.domain.dto.request.RegistrationRequestDto;
import com.sun.librarymanagement.domain.dto.response.SuccessResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import jakarta.mail.MessagingException;

public interface AuthService {

    SuccessResponseDto registration(final RegistrationRequestDto user) throws MessagingException;

    SuccessResponseDto verifyEmail(final String token);

    SuccessResponseDto resendVerification(final String email) throws MessagingException;

    UserResponseDto authentication(final AuthenticationRequestDto user);
}
