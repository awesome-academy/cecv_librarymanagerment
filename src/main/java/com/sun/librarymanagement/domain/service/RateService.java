package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.RateBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface RateService {
    BookResponseDto addBookRating(RateBookRequestDto request, long userId);

    void deleteBookRating(long userId, long bookId);
}
