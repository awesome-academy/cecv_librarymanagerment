package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.security.AppUserDetails;

public interface FollowService {

    PublisherResponseDto followPublisher(Long publisherId, AppUserDetails currentUser);

    PublisherResponseDto unfollowPublisher(Long publisherId, AppUserDetails currentUser);

    AuthorResponseDto followAuthor(Long authorId, AppUserDetails currentUser);

    AuthorResponseDto unfollowAuthor(Long authorId, AppUserDetails currentUser);
}

