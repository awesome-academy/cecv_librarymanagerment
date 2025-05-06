package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.CommentRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.CommentResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.CommentEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.repository.CommentRepository;
import com.sun.librarymanagement.domain.repository.UserRepository;
import com.sun.librarymanagement.domain.service.impl.CommentServiceImpl;
import com.sun.librarymanagement.exception.AppException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sun.librarymanagement.data.TestDataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private UserService userService;

    @Mock
    private TypeMap<BookEntity, BookResponseDto> typeMap;

    @Test
    void addComment_success() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentRequestDto request = defaultCommentRequest();
        CommentResponseDto response = defaultCommentResponse();
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        CommentEntity commentEntity = defaultCommentEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(modelMapper.map(commentEntity, CommentResponseDto.class)).thenReturn(response);
        CommentResponseDto result = commentService.addComment(bookId, userId, request);
        assertEquals(commentEntity.getBody(), result.getBody());
        assertEquals(commentId, result.getId());
    }

    @Test
    void addComment_userNotFound() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentRequestDto request = defaultCommentRequest();
        CommentEntity commentEntity = defaultCommentEntity();
        BookEntity bookEntity = defaultBookEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> commentService.addComment(bookId, userId, request)
        );
    }

    @Test
    void addComment_bookNotFound() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentRequestDto request = defaultCommentRequest();
        CommentEntity commentEntity = defaultCommentEntity();
        BookEntity bookEntity = defaultBookEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> commentService.addComment(bookId, userId, request)
        );
    }

    @Test
    void updateComment_success() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentRequestDto request = defaultCommentRequest();
        CommentResponseDto response = defaultCommentResponse();
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        CommentEntity commentEntity = defaultCommentEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(modelMapper.map(commentEntity, CommentResponseDto.class)).thenReturn(response);
        CommentResponseDto result = commentService.updateComment(bookId, userId, commentId, request);
        verify(commentRepository, times(1)).save(commentEntity);
        assertEquals(commentEntity.getBody(), result.getBody());
        assertEquals(commentId, result.getId());
    }

    @Test
    void updateComment_userNotFound() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentRequestDto request = defaultCommentRequest();
        CommentEntity commentEntity = defaultCommentEntity();
        BookEntity bookEntity = defaultBookEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> commentService.updateComment(bookId, userId, commentId, request)
        );
    }

    @Test
    void updateComment_bookNotFound() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentRequestDto request = defaultCommentRequest();
        CommentEntity commentEntity = defaultCommentEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> commentService.updateComment(bookId, userId, commentId, request)
        );
    }

    @Test
    void updateComment_commentNotFound() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentRequestDto request = defaultCommentRequest();
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> commentService.updateComment(bookId, userId, commentId, request)
        );
    }

    @Test
    void updateComment_commentNotAssociatedWithUser() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        long otherUserId = 2L;
        CommentRequestDto request = defaultCommentRequest();
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        CommentEntity commentEntity = defaultCommentEntity();
        commentEntity.setUser(UserEntity.builder().id(otherUserId).build());
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        assertThrows(AppException.class,
            () -> commentService.updateComment(bookId, userId, commentId, request)
        );
    }

    @Test
    void updateComment_commentNotAssociatedWithBook() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        long otherBookId = 2L;
        CommentRequestDto request = defaultCommentRequest();
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        CommentEntity commentEntity = defaultCommentEntity();
        BookEntity otherBookEntity = new BookEntity();
        otherBookEntity.setId(otherBookId);
        commentEntity.setBookEntity(otherBookEntity);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        assertThrows(AppException.class,
            () -> commentService.updateComment(bookId, userId, commentId, request)
        );
    }

    @Test
    void deleteComment_success() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        CommentEntity commentEntity = defaultCommentEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        commentService.deleteComment(bookId, userId, commentId);
        ArgumentCaptor<CommentEntity> commentCaptor = ArgumentCaptor.forClass(CommentEntity.class);
        verify(commentRepository, times(1)).delete(commentCaptor.capture());
    }

    @Test
    void deleteComment_userNotFound() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentEntity commentEntity = defaultCommentEntity();
        BookEntity bookEntity = defaultBookEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> commentService.deleteComment(bookId, userId, commentId)
        );
    }

    @Test
    void deleteComment_bookNotFound() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        CommentEntity commentEntity = defaultCommentEntity();
        commentEntity.setId(commentId);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> commentService.deleteComment(bookId, userId, commentId)
        );
    }

    @Test
    void deleteComment_commentNotFound() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
        assertThrows(AppException.class,
            () -> commentService.deleteComment(bookId, userId, commentId)
        );
    }

    @Test
    void deleteComment_commentNotAssociatedWithUser() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        long otherUserId = 2L;
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        CommentEntity commentEntity = defaultCommentEntity();
        commentEntity.setUser(UserEntity.builder().id(otherUserId).build());
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        assertThrows(AppException.class,
            () -> commentService.deleteComment(bookId, userId, commentId)
        );
    }

    @Test
    void deleteComment_commentNotAssociatedWithBook() {
        long bookId = 1L;
        long userId = 1L;
        long commentId = 1L;
        long otherBookId = 2L;
        BookEntity bookEntity = defaultBookEntity();
        UserEntity userEntity = defaultUserEntity();
        CommentEntity commentEntity = defaultCommentEntity();
        BookEntity otherBookEntity = new BookEntity();
        otherBookEntity.setId(otherBookId);
        commentEntity.setBookEntity(otherBookEntity);
        when(bookRepository.findByIdWithLock(bookId)).thenReturn(Optional.of(bookEntity));
        when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(userEntity));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        assertThrows(AppException.class,
            () -> commentService.deleteComment(bookId, userId, commentId)
        );
    }

    @Test
    public void getAllComment_withPageNumberExceedsTotalPages_shouldReturnEmptyResult() {
        long bookId = 1L;
        int pageNumber = 100;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CommentEntity> page = new PageImpl<>(Collections.emptyList());
        when(commentRepository.findAllByBookId(anyLong(), eq(pageable))).thenReturn(page);
        PaginatedResponseDto<CommentResponseDto> response = commentService.allComments(bookId, pageNumber, pageSize);
        assertEquals(0, response.getResults().size());
    }

    @Test
    public void getAllComment_shouldReturnAllBooks() {
        long bookId = 1L;
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CommentEntity> page = new PageImpl<>(List.of(new CommentEntity(), new CommentEntity()));
        when(commentRepository.findAllByBookId(anyLong(), eq(pageable))).thenReturn(page);
        PaginatedResponseDto<CommentResponseDto> response = commentService.allComments(bookId, pageNumber, pageSize);
        assertEquals(2, response.getResults().size());
    }

    @Test
    public void getAllComment_shouldReturnEmpty() {
        long bookId = 1L;
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CommentEntity> page = new PageImpl<>(Collections.emptyList());
        when(commentRepository.findAllByBookId(anyLong(), eq(pageable))).thenReturn(page);
        PaginatedResponseDto<CommentResponseDto> response = commentService.allComments(bookId, pageNumber, pageSize);
        assertEquals(0, response.getResults().size());
    }
}
