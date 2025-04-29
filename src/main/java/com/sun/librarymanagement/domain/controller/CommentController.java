package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.CommentRequestDto;
import com.sun.librarymanagement.domain.dto.response.CommentResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.service.CommentService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.COMMENTS)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<CommentResponseDto>> allComment(
        @RequestParam long id,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        return ResponseEntity.ok(commentService.allComments(id, pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
        @RequestParam long id,
        @RequestBody @Valid CommentRequestDto request,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(commentService.addComment(id, currentUser.getId(), request));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
        @RequestParam long id,
        @PathVariable long commentId,
        @RequestBody @Valid CommentRequestDto request,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(commentService.updateComment(id, currentUser.getId(), commentId, request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
        @RequestParam long id,
        @PathVariable long commentId,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        commentService.deleteComment(id, commentId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
