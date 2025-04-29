package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.CommentRequestDto;
import com.sun.librarymanagement.domain.dto.response.CommentResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;

public interface CommentService {

    CommentResponseDto addComment(long id, long currentUserId, CommentRequestDto commentRequest);

    CommentResponseDto updateComment(long id, long currentUserId, long commentId, CommentRequestDto commentRequest);

    void deleteComment(long id, long currentUserId, long commentId);

    PaginatedResponseDto<CommentResponseDto> allComments(long id, int pageNumber, int pageSize);
}
