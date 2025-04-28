package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.SearchBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.service.impl.BookServiceImpl;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.sun.librarymanagement.data.TestDataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private UserService userService;

    @Mock
    private TypeMap<BookEntity, BookResponseDto> typeMap;

    @Test
    public void search_withAllNullParams_shouldReturnAllBooks() {
        SearchBookRequestDto request = new SearchBookRequestDto();
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        Page<BookEntity> page = new PageImpl<>(List.of(new BookEntity(), new BookEntity()));
        when(bookRepository.searchBook(any(), any(), any(), any(), any(), eq(pageable))).thenReturn(page);
        when(modelMapper.map(any(), eq(BookResponseDto.class))).thenReturn(new BookResponseDto());
        PaginatedResponseDto<BookResponseDto> response = bookService.search(request, 0, 10);
        assertEquals(2, response.getResults().size());
        assertEquals(0, response.getPage());
    }

    @Test
    public void search_withPublisherParam_shouldReturnBooksFilteredByPublisher() {
        String publisher = "Spring Publisher";
        SearchBookRequestDto request = new SearchBookRequestDto(publisher, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookEntity> page = new PageImpl<>(List.of(new BookEntity()));
        when(bookRepository.searchBook(eq(publisher), isNull(), isNull(), isNull(), isNull(), eq(pageable)))
            .thenReturn(page);
        when(modelMapper.map(any(), eq(BookResponseDto.class))).thenReturn(new BookResponseDto());
        PaginatedResponseDto<BookResponseDto> response = bookService.search(request, 0, 10);
        assertEquals(1, response.getResults().size());
    }

    @Test
    public void search_withPageNumberExceedsTotalPages_shouldReturnEmptyResult() {
        SearchBookRequestDto request = new SearchBookRequestDto();
        Pageable pageable = PageRequest.of(100, 10);
        Page<BookEntity> page = new PageImpl<>(Collections.emptyList());
        when(bookRepository.searchBook(any(), any(), any(), any(), any(), eq(pageable))).thenReturn(page);
        PaginatedResponseDto<BookResponseDto> response = bookService.search(request, 100, 10);
        assertEquals(0, response.getResults().size());
    }

    @Test
    public void search_withNoMatchingBooks_shouldReturnEmptyResult() {
        SearchBookRequestDto request = new SearchBookRequestDto("NonExistentPublisher", null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookEntity> page = new PageImpl<>(Collections.emptyList());
        when(bookRepository.searchBook(eq("NonExistentPublisher"), isNull(), isNull(), isNull(), isNull(), eq(pageable)))
            .thenReturn(page);
        PaginatedResponseDto<BookResponseDto> response = bookService.search(request, 0, 10);
        assertEquals(0, response.getResults().size());
    }


    @Test
    public void testFavorite_bookNotFound() {
        long bookId = 1L;
        long userId = 2L;
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.empty());
        assertThrows(AppException.class, () -> bookService.favorite(bookId, userId), AppError.BOOK_NOT_FOUND.getMessage());
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
            bookService.favorite(bookId, userId);
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
        BookResponseDto result = bookService.favorite(bookId, userId);
        Assertions.assertNotNull(result);
        verify(bookRepository, times(1)).save(bookEntity);
    }
}
