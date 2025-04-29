package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.CommentRequestDto;
import com.sun.librarymanagement.domain.dto.response.CommentResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.CommentEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.BookRepository;
import com.sun.librarymanagement.domain.repository.CommentRepository;
import com.sun.librarymanagement.domain.repository.UserRepository;
import com.sun.librarymanagement.domain.service.CommentService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CommentResponseDto addComment(long id, long currentUserId, CommentRequestDto commentRequest) {
        BookEntity bookEntity = bookRepository.findByIdWithLock(id).orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        UserEntity userEntity = userRepository.findByIdWithLock(currentUserId)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
        CommentEntity result = commentRepository.save(
            new CommentEntity(commentRequest.getBody(), userEntity, bookEntity)
        );
        return modelMapper.map(result, CommentResponseDto.class);
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(long id, long currentUserId, long commentId, CommentRequestDto commentRequest) {
        CommentEntity commentEntity = validateCommentAssociation(id, currentUserId, commentId);
        commentEntity.setBody(commentRequest.getBody());
        CommentEntity result = commentRepository.save(commentEntity);
        return modelMapper.map(result, CommentResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteComment(long id, long currentUserId, long commentId) {
        CommentEntity commentEntity = validateCommentAssociation(id, currentUserId, commentId);
        commentRepository.delete(commentEntity);
    }

    @Override
    public PaginatedResponseDto<CommentResponseDto> allComments(long id, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CommentEntity> commentEntities = commentRepository.findAllByBookId(id, pageable);
        return new PaginatedResponseDto<>(
            commentEntities.stream().map(comment -> modelMapper.map(comment, CommentResponseDto.class)).toList(),
            pageNumber,
            commentEntities.getTotalPages(),
            commentEntities.getTotalElements()
        );
    }

    private CommentEntity validateCommentAssociation(long bookId, long userId, long commentId) {
        BookEntity bookEntity = bookRepository.findByIdWithLock(bookId)
            .orElseThrow(() -> new AppException(AppError.BOOK_NOT_FOUND));
        UserEntity userEntity = userRepository.findByIdWithLock(userId)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
        CommentEntity commentEntity = commentRepository.findById(commentId)
            .orElseThrow(() -> new AppException(AppError.COMMENT_NOT_FOUND));
        if (!Objects.equals(commentEntity.getUser().getId(), userEntity.getId())) {
            throw new AppException(AppError.COMMENT_NOT_ASSOCIATED_WITH_USER);
        }
        if (!Objects.equals(commentEntity.getBookEntity().getId(), bookEntity.getId())) {
            throw new AppException(AppError.COMMENT_NOT_ASSOCIATED_WITH_BOOK);
        }
        return commentEntity;
    }
}
