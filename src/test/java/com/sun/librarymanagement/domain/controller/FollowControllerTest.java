package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.model.UserRole;
import com.sun.librarymanagement.domain.service.FollowService;
import com.sun.librarymanagement.domain.service.UserService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.security.WebSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.sun.librarymanagement.data.TestDataProvider.defaultAuthorResponse;
import static com.sun.librarymanagement.data.TestDataProvider.defaultPublisherResponse;
import static com.sun.librarymanagement.utils.ApiPaths.FOLLOWS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowController.class)
@AutoConfigureMockMvc
@Import(WebSecurityConfiguration.class)
class FollowControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FollowService followService;

    @MockitoBean
    private UserService userService;

    @Test
    void followPublisher_userNotAuthenticated_shouldReturn401() throws Exception {
        long publisherId = 1L;
        mockMvc.perform(
                post(FOLLOWS + "/publishers/" + publisherId)
                    .with(csrf())
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void followPublisher_userNotFound_shouldReturn404() throws Exception {
        long userId = 1L;
        long publisherId = 1L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(followService.followPublisher(eq(userId), any())).thenThrow(new AppException(AppError.USER_NOT_FOUND));
        mockMvc.perform(
                post(FOLLOWS + "/publishers/" + publisherId)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void followPublisher_validRequest_shouldReturnSuccess() throws Exception {
        long userId = 1L;
        long publisherId = 1L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        PublisherResponseDto response = defaultPublisherResponse();
        response.setFollowing(true);
        when(followService.followPublisher(eq(userId), any())).thenReturn(response);

        mockMvc.perform(
                post(FOLLOWS + "/publishers/" + publisherId)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.getId()))
            .andExpect(jsonPath("$.name").value(response.getName()))
            .andExpect(jsonPath("$.is_following").value(response.isFollowing()));
    }

    @Test
    void unfollowPublisher_userNotAuthenticated_shouldReturn401() throws Exception {
        long publisherId = 1L;
        mockMvc.perform(
                delete(FOLLOWS + "/publishers/" + publisherId)
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void unfollowPublisher_userNotFound_shouldReturn404() throws Exception {
        long userId = 1L;
        long publisherId = 1L;
        when(followService.unfollowPublisher(eq(userId), any())).thenThrow(new AppException(AppError.USER_NOT_FOUND));
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        mockMvc.perform(
                delete(FOLLOWS + "/publishers/" + publisherId)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void unfollowPublisher_validRequest_shouldReturnSuccess() throws Exception {
        long userId = 1L;
        long publisherId = 1L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        PublisherResponseDto response = defaultPublisherResponse();
        response.setFollowing(false);
        when(followService.unfollowPublisher(eq(userId), any())).thenReturn(response);

        mockMvc.perform(
                delete(FOLLOWS + "/publishers/" + publisherId)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(publisherId))
            .andExpect(jsonPath("$.name").value(response.getName()))
            .andExpect(jsonPath("$.is_following").value(response.isFollowing()));
    }

    @Test
    void followAuthor_userNotAuthenticated_shouldReturn401() throws Exception {
        long authorId = 1L;
        mockMvc.perform(
                post(FOLLOWS + "/authors/" + authorId)
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void followAuthor_userNotFound_shouldReturn404() throws Exception {
        long authorId = 1L;
        long userId = 1L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(followService.followAuthor(eq(authorId), any())).thenThrow(new AppException(AppError.USER_NOT_FOUND));
        mockMvc.perform(
                post(FOLLOWS + "/authors/" + authorId)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void followAuthor_validRequest_shouldReturnSuccess() throws Exception {
        long authorId = 1L;
        long userId = 1L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        AuthorResponseDto response = defaultAuthorResponse();
        when(followService.followAuthor(eq(authorId), any())).thenReturn(response);
        mockMvc.perform(
                post(FOLLOWS + "/authors/" + authorId)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(authorId))
            .andExpect(jsonPath("$.name").value(response.getName()))
            .andExpect(jsonPath("$.is_following").value(response.isFollowing()));
    }

    @Test
    void unfollowAuthor_userNotAuthenticated_shouldReturn401() throws Exception {
        long authorId = 1L;
        mockMvc.perform(
                delete("/authors/" + authorId)
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void unfollowAuthor_userNotFound_shouldReturn404() throws Exception {
        long authorId = 1L;
        long userId = 1L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(followService.unfollowAuthor(eq(authorId), any())).thenThrow(new AppException(AppError.USER_NOT_FOUND));
        mockMvc.perform(
                delete(FOLLOWS + "/authors/" + authorId)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void unfollowAuthor_validRequest_shouldReturnSuccess() throws Exception {
        long authorId = 1L;
        long userId = 1L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        AuthorResponseDto response = defaultAuthorResponse();
        when(followService.unfollowAuthor(eq(authorId), any())).thenReturn(response);

        mockMvc.perform(
                delete(FOLLOWS + "/authors/" + authorId)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.getId()))
            .andExpect(jsonPath("$.name").value(response.getName()))
            .andExpect(jsonPath("$.is_following").value(response.isFollowing()));
    }

}
