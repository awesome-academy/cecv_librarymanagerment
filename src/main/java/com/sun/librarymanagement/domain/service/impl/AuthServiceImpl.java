package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.AuthenticationRequestDto;
import com.sun.librarymanagement.domain.dto.request.RegistrationRequestDto;
import com.sun.librarymanagement.domain.dto.response.SuccessResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.model.UserRole;
import com.sun.librarymanagement.domain.repository.UserRepository;
import com.sun.librarymanagement.domain.service.AuthService;
import com.sun.librarymanagement.domain.service.MailService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SuccessResponseDto registration(RegistrationRequestDto user) {
        userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
            .stream()
            .findAny()
            .ifPresent(entity -> {
                throw new AppException(AppError.ACCOUNT_ALREADY_EXISTS);
            });

        String verifyToken = UUID.randomUUID().toString();
        UserEntity userEntity = UserEntity.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .password(passwordEncoder.encode(user.getPassword()))
            .role(UserRole.USER)
            .verifyToken(verifyToken)
            .build();
        userEntity.setIsVerified(true); // TODO: Pending function to send verification email.
        userRepository.save(userEntity);
        /* TODO: Pending function to send verification email.
        mailService.sendVerificationEmail(user.getEmail(), verifyToken);
         */
        return new SuccessResponseDto("Account created successfully. Please check your email to verify your account.");
    }

    @Override
    public SuccessResponseDto verifyEmail(String token) {
        UserEntity userEntity = userRepository.findByVerifyToken(token)
            .orElseThrow(() -> new AppException(AppError.VERIFICATION_TOKEN_INVALID));

        userEntity.setIsVerified(true);
        userEntity.setVerifyToken(null);
        userRepository.save(userEntity);

        return new SuccessResponseDto("Account verified successfully.");
    }

    @Override
    public SuccessResponseDto resendVerification(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        if (!userEntity.getIsVerified()) {
            throw new AppException(AppError.USER_ALREADY_VERIFIED);
        }

        String verifyToken = UUID.randomUUID().toString();
        userEntity.setVerifyToken(verifyToken);
        userRepository.save(userEntity);
        mailService.sendVerificationEmail(email, verifyToken);

        return new SuccessResponseDto("Verification email has been resent.");
    }

    @Override
    public UserResponseDto authentication(AuthenticationRequestDto user) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail())
            .filter(entity -> passwordEncoder.matches(user.getPassword(), entity.getPassword()))
            .orElseThrow(() -> new AppException(AppError.LOGIN_INFO_INVALID));

        if (!userEntity.getIsVerified()) {
            throw new AppException(AppError.ACCOUNT_NOT_VERIFIED);
        } else if (!userEntity.getIsActive()) {
            throw new AppException(AppError.ACCOUNT_NOT_ACTIVE);
        }

        return convertEntityToDto(userEntity);
    }

    private UserResponseDto convertEntityToDto(UserEntity entity) {
        return UserResponseDto.builder()
            .token(jwtUtils.encode(entity.getEmail()))
            .username(entity.getUsername())
            .email(entity.getEmail())
            .build();
    }
}
