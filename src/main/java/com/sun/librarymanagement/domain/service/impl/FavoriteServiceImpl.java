package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.service.FavoriteService;
import com.sun.librarymanagement.domain.service.UserService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.sun.librarymanagement.utils.MapperConverter.isFavoritedConverter;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    @Transactional
    public BookResponseDto favorite(long id, long currentUserId) {
        BookEntity currentBook = bookRepository.findByIdWithLock(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        UserEntity userEntity = userService.getUserById(currentUserId);
        boolean isFavorite = Optional.ofNullable(currentBook.getFavorites())
                .map(favorites -> favorites.stream().anyMatch(e -> e.getId().equals(userEntity.getId())))
                .orElse(false);
        if (isFavorite) {
            throw new AppException(AppError.BOOK_ALREADY_FAVORITED);
        }
        Set<UserEntity> favorites = Optional.ofNullable(currentBook.getFavorites())
                .map(HashSet::new)
                .orElseGet(HashSet::new);
        favorites.add(userEntity);
        currentBook.setFavorites(favorites);
        BookEntity book = bookRepository.save(currentBook);
        return convertToBookResponseDto(book, currentUserId);
    }

    @Override
    @Transactional
    public void unfavorite(long id, long currentUserId) {
        BookEntity currentBook = bookRepository.findByIdWithLock(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        UserEntity userEntity = userService.getUserById(currentUserId);
        boolean isFavorite = Optional.ofNullable(currentBook.getFavorites())
                .map(favorites -> favorites.stream().anyMatch(e -> e.getId().equals(userEntity.getId())))
                .orElse(false);
        if (!isFavorite) {
            throw new AppException(AppError.FAVORITE_NOT_FOUND);
        }
        Set<UserEntity> favorites = Optional.of(currentBook.getFavorites())
                .map(HashSet::new)
                .orElseGet(HashSet::new);
        favorites.remove(userEntity);
        currentBook.setFavorites(favorites);
        bookRepository.save(currentBook);
    }

    public PaginatedResponseDto<BookResponseDto> getUserFavoriteBooks(int pageNumber, int pageSize, long currentUserId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<BookEntity> page = bookRepository.getFavoriteBooks(currentUserId, pageable);
        return new PaginatedResponseDto<>(
                page.stream().map(book -> convertToBookResponseDto(book, currentUserId)).toList(),
                pageNumber,
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    private BookResponseDto convertToBookResponseDto(BookEntity bookEntity, long currentUserId) {
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
