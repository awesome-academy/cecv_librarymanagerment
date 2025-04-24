package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.AuthorRequestDto;
import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.security.AppUserDetails;

public interface AuthorService {

    AuthorResponseDto addAuthor(AuthorRequestDto authorRequestDto);

    PaginatedResponseDto<AuthorResponseDto> getAuthors(int pageNumber, int pageSize);

    AuthorResponseDto getAuthor(long id, AppUserDetails currentUser);

    AuthorResponseDto updateAuthor(long id, AuthorRequestDto authorRequestDto);

    void deleteAuthor(long id);
}
