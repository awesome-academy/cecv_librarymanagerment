package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.BookRequestDto;
import com.sun.librarymanagement.domain.dto.request.SearchBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.security.AppUserDetails;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    BookResponseDto addBook(BookRequestDto request);

    BookResponseDto getBook(long id, AppUserDetails currentUser);

    PaginatedResponseDto<BookResponseDto> getBooks(int pageNumber, int pageSize);

    BookResponseDto updateBook(long id, BookRequestDto request);

    void deleteBook(long id);

    PaginatedResponseDto<BookResponseDto> search(SearchBookRequestDto request, int pageNumber, int pageSize);

    BookResponseDto favorite(long id, Long currentUserId);

    void unfavorite(long id, Long currentUserId);
}
