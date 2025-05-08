package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface FavoriteService {
    BookResponseDto favorite(long id, long currentUserId);

    void unfavorite(long id, long currentUserId);

    PaginatedResponseDto<BookResponseDto> getUserFavoriteBooks(int pageNumber, int pageSize, long currentUserId);
}
