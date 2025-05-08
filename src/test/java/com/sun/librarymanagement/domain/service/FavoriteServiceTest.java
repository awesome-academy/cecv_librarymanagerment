package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.service.impl.FavoriteServiceImpl;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.Optional;
import java.util.Set;

import static com.sun.librarymanagement.data.TestDataProvider.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Mock
    private UserService userService;

    @Mock
    private TypeMap<BookEntity, BookResponseDto> typeMap;

    @Test
    public void testFavorite_bookNotFound() {
        long bookId = 1L;
        long userId = 2L;
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.empty());
        assertThrows(AppException.class, () -> favoriteService.favorite(bookId, userId), AppError.BOOK_NOT_FOUND.getMessage());
    }

    @Test
    public void testFavorite_bookAlreadyFavorited() {
        long bookId = 1L;
        long userId = 2L;
        UserEntity userEntity = defaultUserEntity();
        BookEntity bookEntity = defaultBookEntity();
        bookEntity.setFavorites(Set.of(userEntity));
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userService.getUserById(userId)).thenReturn(userEntity);
        assertThrows(AppException.class, () -> {
            favoriteService.favorite(bookId, userId);
        }, AppError.BOOK_ALREADY_FAVORITED.getMessage());
    }

    @Test
    public void testFavorite_success() {
        long bookId = 1L;
        long userId = 2L;
        UserEntity userEntity = defaultUserEntity();
        BookEntity bookEntity = defaultBookEntity();
        bookEntity.setId(bookId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userService.getUserById(userId)).thenReturn(userEntity);
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(modelMapper.typeMap(BookEntity.class, BookResponseDto.class)).thenReturn(typeMap);
        when(typeMap.addMappings(any(ExpressionMap.class))).thenReturn(typeMap);
        when(typeMap.map(any())).thenReturn(defaultBookResponse());
        BookResponseDto result = favoriteService.favorite(bookId, userId);
        Assertions.assertNotNull(result);
        verify(bookRepository, times(1)).save(bookEntity);
    }

    @Test
    void removeFavorite_BookNotFound() {
        long bookId = 1L;
        long userId = 2L;
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> favoriteService.favorite(bookId, userId), AppError.BOOK_NOT_FOUND.getMessage()
        );
    }

    @Test
    void removeFavorite_FavoriteNotFound() {
        long bookId = 1L;
        long userId = 2L;
        UserEntity userEntity = defaultUserEntity();
        BookEntity bookEntity = defaultBookEntity();
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userService.getUserById(userId)).thenReturn(userEntity);
        assertThrows(AppException.class, () -> {
            favoriteService.unfavorite(bookId, userId);
        }, AppError.FAVORITE_NOT_FOUND.getMessage());
    }

    @Test
    void removeFavorite_Success_ShouldRemoveFavoriteAndSave() {
        Long bookId = 1L;
        Long userId = 2L;
        UserEntity userEntity = defaultUserEntity();
        BookEntity bookEntity = defaultBookEntity();
        bookEntity.setId(bookId);
        bookEntity.setFavorites(Set.of(userEntity));
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userService.getUserById(userId)).thenReturn(userEntity);
        favoriteService.unfavorite(bookId, userId);
        ArgumentCaptor<BookEntity> bookCaptor = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        BookEntity savedBook = bookCaptor.getValue();
        assertFalse(savedBook.getFavorites().contains(userEntity));
    }
}
