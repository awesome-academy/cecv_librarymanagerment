package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.BookRequest;
import com.sun.librarymanagement.domain.dto.response.BookResponse;
import com.sun.librarymanagement.domain.dto.response.BooksResponse;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    BookResponse addBook(BookRequest request);

    BookResponse getBook(long id);

    BooksResponse getBooks(int pageNumber, int pageSize);

    BookResponse updateBook(long id, BookRequest request);

    void deleteBook(long id);
}
