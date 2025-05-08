package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.entity.FollowEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.FollowRepository;
import com.sun.librarymanagement.domain.service.impl.FollowServiceImpl;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.sun.librarymanagement.data.TestDataProvider.defaultAuthorResponse;
import static com.sun.librarymanagement.data.TestDataProvider.defaultPublisherResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @InjectMocks
    private FollowServiceImpl followService;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private PublisherService publisherService;

    @Mock
    private AuthorService authorService;

    @Mock
    private UserService userService;


    @Test
    void followPublisher_userNotFound() {
        long userId = 1L;
        long publisherId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userService.getUserById(userId)).thenThrow(new AppException(AppError.USER_NOT_FOUND));
        assertThrows(AppException.class, () -> followService.followPublisher(publisherId, userDetails));
    }

    @Test
    void followPublisher_publisherAlreadyFollowed() {
        long userId = 1L;
        long publisherId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        when(userService.getUserById(userId)).thenReturn(userEntity);
        PublisherResponseDto response = defaultPublisherResponse();
        response.setFollowing(true);
        when(publisherService.getPublisher(publisherId, userDetails)).thenReturn(response);
        AppException ex = assertThrows(AppException.class, () -> followService.followPublisher(publisherId, userDetails));
        assertEquals(AppError.PUBLISHER_ALREADY_FOLLOWED, ex.getAppError());
    }

    @Test
    void followPublisher_shouldSaveFollowAndReturnDto() {
        long userId = 1L;
        long publisherId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        when(userService.getUserById(userId)).thenReturn(userEntity);
        PublisherResponseDto response = defaultPublisherResponse();
        when(publisherService.getPublisher(publisherId, userDetails)).thenReturn(response);
        PublisherResponseDto result = followService.followPublisher(publisherId, userDetails);
        verify(followRepository).save(any(FollowEntity.class));
        assertTrue(result.isFollowing());
    }

    @Test
    void unfollowPublisher_userNotFound() {
        long publisherId = 100L;
        long userId = 1L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userService.getUserById(userId)).thenThrow(new AppException(AppError.USER_NOT_FOUND));
        assertThrows(AppException.class, () -> followService.unfollowPublisher(publisherId, userDetails));
    }

    @Test
    void unfollowPublisher_publisherNotFollowed() {
        long userId = 1L;
        long publisherId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        when(userService.getUserById(userId)).thenReturn(userEntity);
        PublisherResponseDto response = defaultPublisherResponse();
        when(publisherService.getPublisher(publisherId, userDetails)).thenReturn(response);
        AppException ex = assertThrows(AppException.class, () -> followService.unfollowPublisher(publisherId, userDetails));
        assertEquals(AppError.PUBLISHER_NOT_FOLLOWED, ex.getAppError());
    }

    @Test
    void unfollowPublisher_shouldDeleteFollowAndReturnDto() {
        long userId = 1L;
        long publisherId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        when(userService.getUserById(userId)).thenReturn(userEntity);
        PublisherResponseDto response = defaultPublisherResponse();
        response.setFollowing(true);
        when(publisherService.getPublisher(publisherId, userDetails)).thenReturn(response);
        PublisherResponseDto result = followService.unfollowPublisher(publisherId, userDetails);
        verify(followRepository).delete(any(FollowEntity.class));
        assertFalse(result.isFollowing());
    }

    @Test
    void followAuthor_userNotFound() {
        long userId = 1L;
        long authorId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userService.getUserById(userId)).thenThrow(new AppException(AppError.USER_NOT_FOUND));
        assertThrows(AppException.class, () -> followService.followAuthor(authorId, userDetails));
    }

    @Test
    void followAuthor_authorAlreadyFollowed() {
        long userId = 1L;
        long authorId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        when(userService.getUserById(userId)).thenReturn(userEntity);
        AuthorResponseDto response = defaultAuthorResponse();
        response.setFollowing(true);
        when(authorService.getAuthor(authorId, userDetails)).thenReturn(response);
        AppException ex = assertThrows(AppException.class, () -> followService.followAuthor(authorId, userDetails));
        assertEquals(AppError.AUTHOR_ALREADY_FOLLOWED, ex.getAppError());
    }

    @Test
    void followAuthor_shouldSaveFollowAndReturnDto() {
        long userId = 1L;
        long authorId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        when(userService.getUserById(userId)).thenReturn(userEntity);
        AuthorResponseDto response = defaultAuthorResponse();
        when(authorService.getAuthor(authorId, userDetails)).thenReturn(response);
        AuthorResponseDto result = followService.followAuthor(authorId, userDetails);
        verify(followRepository).save(any(FollowEntity.class));
        assertTrue(result.isFollowing());
    }

    @Test
    void unfollowAuthor_userNotFound() {
        long userId = 1L;
        long authorId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userService.getUserById(userId)).thenThrow(new AppException(AppError.USER_NOT_FOUND));
        assertThrows(AppException.class, () -> followService.unfollowAuthor(authorId, userDetails));
    }

    @Test
    void unfollowAuthor_authorNotFollowed() {
        long userId = 1L;
        long authorId = 100L;
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        when(userService.getUserById(userId)).thenReturn(userEntity);
        AuthorResponseDto response = defaultAuthorResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(authorService.getAuthor(authorId, userDetails)).thenReturn(response);
        AppException ex = assertThrows(AppException.class, () -> followService.unfollowAuthor(authorId, userDetails));
        assertEquals(AppError.AUTHOR_NOT_FOLLOWED, ex.getAppError());
    }

    @Test
    void unfollowAuthor_shouldDeleteFollowAndReturnDto() {
        long userId = 1L;
        long authorId = 100L;
        UserEntity userEntity = UserEntity.builder().id(userId).build();
        when(userService.getUserById(userId)).thenReturn(userEntity);
        AuthorResponseDto response = defaultAuthorResponse();
        response.setFollowing(true);
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(authorService.getAuthor(authorId, userDetails)).thenReturn(response);
        AuthorResponseDto result = followService.unfollowAuthor(authorId, userDetails);
        verify(followRepository).delete(any(FollowEntity.class));
        assertFalse(result.isFollowing());
    }
}
