package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.model.UserRole;
import com.sun.librarymanagement.domain.repository.UserRepository;
import com.sun.librarymanagement.domain.service.UserService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public PaginatedResponseDto<UserResponseDto> getUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<UserEntity> page = userRepository.findByRole(UserRole.USER, pageable);

        return new PaginatedResponseDto<>(
            convertToUserList(page),
            pageNumber,
            page.getTotalPages(),
            page.getTotalElements()
        );
    }

    @Override
    public UserResponseDto getUser(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        return convertEntityToDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto activeUser(Long id) {
        UserEntity user = userRepository.findByIdWithLock(id)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        user.setIsActive(true);
        userRepository.save(user);

        return convertEntityToDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto inactiveUser(Long id) {
        UserEntity user = userRepository.findByIdWithLock(id)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        user.setIsActive(false);
        userRepository.save(user);

        return convertEntityToDto(user);
    }

    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
    }

    private UserResponseDto convertEntityToDto(UserEntity entity) {
        return UserResponseDto.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .isActive(entity.getIsActive())
            .isVerified(entity.getIsVerified())
            .build();
    }

    private List<UserResponseDto> convertToUserList(Page<UserEntity> userEntities) {
        return userEntities.stream()
            .map(this::convertEntityToDto)
            .collect(Collectors.toList());
    }
}
