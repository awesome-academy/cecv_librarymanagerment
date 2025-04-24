package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.service.FollowService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.FOLLOWS)
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/publishers/{publisherId}")
    public ResponseEntity<PublisherResponseDto> followPublisher(
        @PathVariable Long publisherId,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(followService.followPublisher(publisherId, currentUser));
    }

    @DeleteMapping("/publishers/{publisherId}")
    public ResponseEntity<PublisherResponseDto> unfollowPublisher(
        @PathVariable Long publisherId,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(followService.unfollowPublisher(publisherId, currentUser));
    }

    @PostMapping("/authors/{authorId}")
    public ResponseEntity<AuthorResponseDto> followAuthor(
        @PathVariable Long authorId,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(followService.followAuthor(authorId, currentUser));
    }

    @DeleteMapping("/authors/{authorId}")
    public ResponseEntity<AuthorResponseDto> unfollowAuthor(
        @PathVariable Long authorId,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(followService.unfollowAuthor(authorId, currentUser));
    }
}
