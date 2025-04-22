package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.BookRequest;
import com.sun.librarymanagement.domain.dto.response.BookResponse;
import com.sun.librarymanagement.domain.dto.response.BooksResponse;
import com.sun.librarymanagement.domain.entity.AuthorEntity;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.CategoryEntity;
import com.sun.librarymanagement.domain.entity.PublisherEntity;
import com.sun.librarymanagement.domain.repository.AuthorRepository;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.repository.CategoryRepository;
import com.sun.librarymanagement.domain.repository.PublisherRepository;
import com.sun.librarymanagement.domain.service.BookService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookResponse addBook(@NotNull BookRequest request) {
        PublisherEntity publisher = publisherRepository.findById(request.getPublisherId()).orElseThrow(() -> new AppException(AppError.PUBLISHER_NOT_FOUND));
        AuthorEntity author = authorRepository.findById(request.getAuthorId()).orElseThrow(() -> new AppException(AppError.AUTHOR_NOT_FOUND));
        Set<CategoryEntity> categories = request.getCategoryIds().stream().map(categoryId -> categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(AppError.CATEGORY_NOT_FOUND))).collect(Collectors.toSet());
        BookEntity book = bookRepository.save(new BookEntity(request.getName(), request.getDescription(), request.getImageUrl(), publisher, request.getQuantity(), 0, author, categories));
        return modelMapper.map(book, BookResponse.class);
    }

    @Override
    public BookResponse getBook(long id) {
        BookEntity book = bookRepository.findById(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        return modelMapper.map(book, BookResponse.class);
    }

    @Override
    public BooksResponse getBooks(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<BookEntity> page = bookRepository.findAll(pageable);
        return new BooksResponse(
                page.stream().map(book -> modelMapper.map(book, BookResponse.class)).toList(),
                pageNumber,
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public BookResponse updateBook(long id, @NotNull BookRequest request) {
        BookEntity currentBook = bookRepository.findById(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        PublisherEntity publisher = publisherRepository.findById(request.getPublisherId()).orElseThrow(() -> new AppException(AppError.PUBLISHER_NOT_FOUND));
        AuthorEntity author = authorRepository.findById(request.getAuthorId()).orElseThrow(() -> new AppException(AppError.AUTHOR_NOT_FOUND));
        Set<CategoryEntity> categories = request.getCategoryIds().stream().map(categoryId -> categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(AppError.CATEGORY_NOT_FOUND))).collect(Collectors.toSet());
        currentBook.setName(request.getName());
        currentBook.setDescription(request.getDescription());
        currentBook.setImageUrl(request.getImageUrl());
        currentBook.setQuantity(request.getQuantity());
        currentBook.setPublisher(publisher);
        currentBook.setAuthor(author);
        currentBook.setCategories(categories);
        BookEntity book = bookRepository.save(currentBook);
        return modelMapper.map(book, BookResponse.class);
    }

    @Override
    public void deleteBook(long id) {
        bookRepository.findById(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        bookRepository.deleteById(id);
    }
}
