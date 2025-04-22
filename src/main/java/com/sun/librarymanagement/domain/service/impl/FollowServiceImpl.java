package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.entity.FollowEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.model.FollowInfo;
import com.sun.librarymanagement.domain.model.FollowType;
import com.sun.librarymanagement.domain.repository.FollowRepository;
import com.sun.librarymanagement.domain.service.AuthorService;
import com.sun.librarymanagement.domain.service.FollowService;
import com.sun.librarymanagement.domain.service.PublisherService;
import com.sun.librarymanagement.domain.service.UserService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    private final PublisherService publisherService;

    private final AuthorService authorService;

    private final UserService userService;

    @Override
    public PublisherResponseDto followPublisher(Long publisherId, AppUserDetails currentUser) {
        UserEntity currentUserEntity = userService.getUserById(currentUser.getId());
        PublisherResponseDto publisher = publisherService.getPublisher(publisherId, currentUser);
        if (publisher.isFollowing()) {
            throw new AppException(AppError.PUBLISHER_ALREADY_FOLLOWED);
        }
        FollowInfo followInfo = new FollowInfo(currentUser.getId(), publisherId, FollowType.PUBLISHER);
        followRepository.save(new FollowEntity(followInfo, currentUserEntity));
        publisher.setFollowing(true);
        return publisher;
    }

    @Override
    public PublisherResponseDto unfollowPublisher(Long publisherId, AppUserDetails currentUser) {
        UserEntity currentUserEntity = userService.getUserById(currentUser.getId());
        PublisherResponseDto publisher = publisherService.getPublisher(publisherId, currentUser);
        if (!publisher.isFollowing()) {
            throw new AppException(AppError.PUBLISHER_NOT_FOLLOWED);
        }
        FollowInfo followInfo = new FollowInfo(currentUser.getId(), publisherId, FollowType.PUBLISHER);
        followRepository.delete(new FollowEntity(followInfo, currentUserEntity));
        publisher.setFollowing(false);
        return publisher;
    }

    @Override
    public AuthorResponseDto followAuthor(Long authorId, AppUserDetails currentUser) {
        UserEntity currentUserEntity = userService.getUserById(currentUser.getId());
        AuthorResponseDto author = authorService.getAuthor(authorId, currentUser);
        if (author.isFollowing()) {
            throw new AppException(AppError.AUTHOR_ALREADY_FOLLOWED);
        }
        FollowInfo followInfo = new FollowInfo(currentUser.getId(), authorId, FollowType.AUTHOR);
        followRepository.save(new FollowEntity(followInfo, currentUserEntity));
        author.setFollowing(true);
        return author;
    }

    @Override
    public AuthorResponseDto unfollowAuthor(Long authorId, AppUserDetails currentUser) {
        UserEntity currentUserEntity = userService.getUserById(currentUser.getId());
        AuthorResponseDto author = authorService.getAuthor(authorId, currentUser);
        if (!author.isFollowing()) {
            throw new AppException(AppError.AUTHOR_NOT_FOLLOWED);
        }
        FollowInfo followInfo = new FollowInfo(currentUser.getId(), authorId, FollowType.PUBLISHER);
        followRepository.delete(new FollowEntity(followInfo, currentUserEntity));
        author.setFollowing(false);
        return author;
    }
}
