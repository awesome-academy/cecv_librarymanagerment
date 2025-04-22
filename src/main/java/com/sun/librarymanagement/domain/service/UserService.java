package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.entity.UserEntity;

public interface UserService {

    UserResponseDto.Multiple getUsers(final int pageNumber, final int pageSize);

    UserResponseDto getUser(final Long id);

    UserResponseDto activeUser(final Long id);

    UserResponseDto inactiveUser(final Long id);

    UserEntity getUserById(Long userId);
}
