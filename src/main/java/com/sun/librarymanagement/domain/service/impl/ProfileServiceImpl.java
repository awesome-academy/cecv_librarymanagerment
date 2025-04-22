package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.ChangeEmailRequestDto;
import com.sun.librarymanagement.domain.dto.request.ChangePasswordRequestDto;
import com.sun.librarymanagement.domain.dto.request.UpdateProfileRequestDto;
import com.sun.librarymanagement.domain.dto.response.SuccessResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.UserRepository;
import com.sun.librarymanagement.domain.service.ProfileService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto getCurrentUserProfile(AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        return convertEntityToDto(userEntity);
    }

    @Transactional
    @Override
    public UserResponseDto updateCurrentUserProfile(UpdateProfileRequestDto user, AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findByIdWithLock(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        if (user.getUsername() != null) {
            userRepository.findByUsername(user.getUsername())
                .filter(entity -> !entity.getId().equals(userEntity.getId()))
                .ifPresent(entity -> {
                    throw new AppException(AppError.USERNAME_ALREADY_EXISTS);
                });
            userEntity.setUsername(user.getUsername());
        }
        userRepository.save(userEntity);

        return convertEntityToDto(userEntity);
    }

    @Transactional
    @Override
    public SuccessResponseDto changeEmail(ChangeEmailRequestDto request, AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findByIdWithLock(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        userRepository.findByEmail(request.getEmail())
            .filter(entity -> !entity.getId().equals(userEntity.getId()))
            .ifPresent(entity -> {
                throw new AppException(AppError.EMAIL_ALREADY_EXISTS);
            });
        userEntity.setEmail(request.getEmail());
        userRepository.save(userEntity);

        return new SuccessResponseDto("Email updated successfully. Please log in again.");
    }

    @Transactional
    @Override
    public SuccessResponseDto changePassword(ChangePasswordRequestDto request, AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findByIdWithLock(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), userEntity.getPassword())) {
            throw new AppException(AppError.PASSWORD_INVALID);
        }
        userEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(userEntity);

        return new SuccessResponseDto("Password updated successfully.");
    }

    private UserResponseDto convertEntityToDto(UserEntity entity) {
        return UserResponseDto.builder()
            .username(entity.getUsername())
            .email(entity.getEmail())
            .build();
    }
}
