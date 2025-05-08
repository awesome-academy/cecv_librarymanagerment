package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.model.UserRole;
import com.sun.librarymanagement.domain.service.FavoriteService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.sun.librarymanagement.data.TestDataProvider.defaultBookResponse;
import static com.sun.librarymanagement.utils.ApiPaths.BOOKS;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
class FavoriteControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoriteService favoriteService;

    @Test
    void favorite_success() throws Exception {
        BookResponseDto response = defaultBookResponse();
        long bookId = 1L;
        long userId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(favoriteService.favorite(bookId, userId)).thenReturn(response);
        mockMvc.perform(post(BOOKS + "/" + bookId + "/favorite")
            .with(csrf())
            .with(user(userDetails))
        ).andExpect(status().isOk());
    }

    @Test
    void favorite_bookNotFound_shouldReturn404() throws Exception {
        long bookId = 1L;
        long userId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(favoriteService.favorite(anyLong(), anyLong()))
            .thenThrow(new AppException(AppError.BOOK_NOT_FOUND));
        mockMvc.perform(
                post(BOOKS + "/" + bookId + "/favorite")
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void favorite_noAuthentication_shouldReturn401() throws Exception {
        long bookId = 1L;
        SecurityContextHolder.clearContext();
        mockMvc.perform(
                post(BOOKS + "/" + bookId + "/favorite")
                    .contentType(APPLICATION_JSON)
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void favorite_alreadyFavorited_shouldReturn409() throws Exception {
        long bookId = 1L;
        long userId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(favoriteService.favorite(anyLong(), anyLong()))
            .thenThrow(new AppException(AppError.BOOK_ALREADY_FAVORITED));
        mockMvc.perform(
                post(BOOKS + "/" + bookId + "/favorite")
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isConflict());
    }

    @Test
    void unfavorite_success() throws Exception {
        long bookId = 1L;
        long userId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        Mockito.doNothing().when(favoriteService).unfavorite(bookId, userId);
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete(BOOKS + "/" + bookId + "/favorite")
                .with(csrf())
                .with(user(userDetails))
        ).andExpect(status().isNoContent());
    }

    @Test
    void unfavorite_bookNotFound_shouldReturn404() throws Exception {
        long bookId = 1L;
        long userId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        mockSecurityContext(userDetails);
        doThrow(new AppException(AppError.BOOK_NOT_FOUND))
            .when(favoriteService)
            .unfavorite(anyLong(), anyLong());
        mockMvc.perform(
                delete(BOOKS + "/" + bookId + "/favorite")
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void unfavorite_userNotAuthenticated_shouldReturn401() throws Exception {
        long bookId = 1L;
        SecurityContextHolder.clearContext();
        mockMvc.perform(
                delete(BOOKS + "/" + bookId + "/favorite")
                    .contentType(APPLICATION_JSON)
            )
            .andExpect(status().isUnauthorized());
    }
}
