package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.BookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.BooksResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    BookResponseDto addBook(BookRequestDto request);

    BookResponseDto getBook(long id);

    BooksResponseDto getBooks(int pageNumber, int pageSize);

    BookResponseDto updateBook(long id, BookRequestDto request);

    void deleteBook(long id);
}
