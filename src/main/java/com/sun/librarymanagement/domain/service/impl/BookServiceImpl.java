package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.BookRequestDto;
import com.sun.librarymanagement.domain.dto.request.SearchBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.entity.*;
import com.sun.librarymanagement.domain.repository.AuthorRepository;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.repository.CategoryRepository;
import com.sun.librarymanagement.domain.repository.PublisherRepository;
import com.sun.librarymanagement.domain.service.BookService;
import com.sun.librarymanagement.domain.service.UserService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sun.librarymanagement.utils.MapperConverter.isFavoritedConverter;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public BookResponseDto addBook(@NotNull BookRequestDto request) {
        PublisherEntity publisher = publisherRepository.findById(request.getPublisherId()).orElseThrow(() -> new AppException(AppError.PUBLISHER_NOT_FOUND));
        AuthorEntity author = authorRepository.findById(request.getAuthorId()).orElseThrow(() -> new AppException(AppError.AUTHOR_NOT_FOUND));
        Set<CategoryEntity> categories = request.getCategoryIds().stream().map(categoryId -> categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(AppError.CATEGORY_NOT_FOUND))).collect(Collectors.toSet());
        BookEntity book = bookRepository.save(BookEntity.builder().name(request.getName()).description(request.getDescription()).imageUrl(request.getImageUrl()).publisher(publisher).quantity(request.getQuantity()).availableQuantity(request.getQuantity()).author(author).categories(categories).build());
        return modelMapper.map(book, BookResponseDto.class);
    }

    @Override
    public BookResponseDto getBook(long id, AppUserDetails currentUser) {
        BookEntity book = bookRepository.findById(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        if (currentUser == null) {
            return modelMapper.map(book, BookResponseDto.class);
        }
        return convertToBookResponseDto(book, currentUser.getId());
    }

    @Override
    public PaginatedResponseDto<BookResponseDto> getBooks(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<BookEntity> page = bookRepository.findAll(pageable);
        return new PaginatedResponseDto<>(
            page.stream().map(book -> modelMapper.map(book, BookResponseDto.class)).toList(),
            pageNumber,
            page.getTotalPages(),
            page.getTotalElements()
        );
    }

    @Override
    @Transactional
    public BookResponseDto updateBook(long id, @NotNull BookRequestDto request) {
        BookEntity currentBook = bookRepository.findByIdWithLock(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        PublisherEntity publisher = publisherRepository.findById(request.getPublisherId()).orElseThrow(() -> new AppException(AppError.PUBLISHER_NOT_FOUND));
        AuthorEntity author = authorRepository.findById(request.getAuthorId()).orElseThrow(() -> new AppException(AppError.AUTHOR_NOT_FOUND));
        Set<CategoryEntity> categories = request.getCategoryIds().stream().map(categoryId -> categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(AppError.CATEGORY_NOT_FOUND))).collect(Collectors.toSet());
        int availableQuantity = Math.max(currentBook.getAvailableQuantity() + request.getQuantity() - currentBook.getQuantity(), 0);
        currentBook.setName(request.getName());
        currentBook.setDescription(request.getDescription());
        currentBook.setImageUrl(request.getImageUrl());
        currentBook.setQuantity(request.getQuantity());
        currentBook.setAvailableQuantity(availableQuantity);
        currentBook.setPublisher(publisher);
        currentBook.setAuthor(author);
        currentBook.setCategories(categories);
        BookEntity book = bookRepository.save(currentBook);
        return modelMapper.map(book, BookResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteBook(long id) {
        BookEntity currentBook = bookRepository.findByIdWithLock(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        currentBook.setDeletedAt(LocalDateTime.now());
        bookRepository.save(currentBook);
    }

    @Override
    @Transactional
    public PaginatedResponseDto<BookResponseDto> search(
        SearchBookRequestDto request,
        int pageNumber,
        int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<BookEntity> page = bookRepository.searchBook(
            request.getPublisher(),
            request.getCategory(),
            request.getAuthor(),
            request.getName(),
            request.getDescription(),
            pageable
        );
        return new PaginatedResponseDto<>(
            page.stream().map(book -> modelMapper.map(book, BookResponseDto.class)).toList(),
            pageNumber,
            page.getTotalPages(),
            page.getTotalElements()
        );
    }

    @Override
    @Transactional
    public BookResponseDto favorite(long id, Long currentUserId) {
        BookEntity currentBook = bookRepository.findByIdWithLock(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        UserEntity userEntity = userService.getUserById(currentUserId);
        boolean isFavorite = currentBook.getFavorites() != null &&
            currentBook.getFavorites().stream().anyMatch((e) -> e.getId().equals(userEntity.getId()));
        if (isFavorite) {
            throw new AppException(AppError.BOOK_ALREADY_FAVORITED);
        }
        Set<UserEntity> favorites = new HashSet<>(
            currentBook.getFavorites() != null ? currentBook.getFavorites() : Set.of()
        );
        favorites.add(userEntity);
        currentBook.setFavorites(favorites);
        BookEntity book = bookRepository.save(currentBook);
        return convertToBookResponseDto(book, currentUserId);
    }

    @Override
    @Transactional
    public void unfavorite(long id, Long currentUserId) {
        BookEntity currentBook = bookRepository.findByIdWithLock(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        UserEntity userEntity = userService.getUserById(currentUserId);
        boolean isFavorite = currentBook.getFavorites().stream().anyMatch((e) -> e.getId().equals(userEntity.getId()));
        if (!isFavorite) {
            throw new AppException(AppError.FAVORITE_NOT_FOUND);
        }
        currentBook.getFavorites().remove(userEntity);
        bookRepository.save(currentBook);
    }

    private BookResponseDto convertToBookResponseDto(BookEntity bookEntity, Long currentUserId) {
        return modelMapper
            .typeMap(BookEntity.class, BookResponseDto.class)
            .addMappings(
                mapper -> mapper
                    .using(isFavoritedConverter(bookEntity, currentUserId))
                    .map(src -> src, BookResponseDto::setFavorited)
            )
            .map(bookEntity);
    }
}
