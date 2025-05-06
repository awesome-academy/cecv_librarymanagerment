package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.CommentRequestDto;
import com.sun.librarymanagement.domain.dto.response.CommentResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.model.UserRole;
import com.sun.librarymanagement.domain.service.CommentService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.sun.librarymanagement.data.TestDataProvider.*;
import static com.sun.librarymanagement.utils.ApiPaths.COMMENTS;
import static com.sun.librarymanagement.utils.Constant.PAGE_NUMBER_PARAM;
import static com.sun.librarymanagement.utils.Constant.PAGE_SIZE_PARAM;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Test
    void allComment_validParams_shouldReturnSuccess() throws Exception {
        long bookId = 10L;
        PaginatedResponseDto<CommentResponseDto> response = defaultPaginatedCommentResponse();
        when(commentService.allComments(anyLong(), anyInt(), anyInt()))
            .thenReturn(response);
        mockMvc.perform(
                get(COMMENTS)
                    .contentType(APPLICATION_JSON)
                    .param("id", String.valueOf(bookId))
                    .param(PAGE_NUMBER_PARAM, String.valueOf(DEFAULT_PAGE_NUMBER))
                    .param(PAGE_SIZE_PARAM, String.valueOf(DEFAULT_PAGE_SIZE))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.page", is((int) response.getPage())))
            .andExpect(jsonPath("$.total_pages", is((int) response.getTotalPages())))
            .andExpect(jsonPath("$.total_results", is((int) response.getTotalResults())))
            .andExpect(jsonPath("$.results", hasSize(1)))
            .andExpect(jsonPath("$.results[0].id", is((int) response.getResults().get(0).getId())))
            .andExpect(jsonPath("$.results[0].body", is(response.getResults().get(0).getBody())));
    }

    @Test
    void allComment_invalidPageParams_shouldReturn422() throws Exception {
        long bookId = 10L;
        PaginatedResponseDto<CommentResponseDto> response = defaultPaginatedCommentResponse();
        when(commentService.allComments(anyLong(), anyInt(), anyInt())).thenReturn(response);
        mockMvc.perform(
                get(COMMENTS)
                    .contentType(APPLICATION_JSON)
                    .param("id", String.valueOf(bookId))
                    .param(PAGE_NUMBER_PARAM, "-1")
                    .param(PAGE_SIZE_PARAM, "-1")
            )
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void addComment_validRequest_shouldReturnSuccess() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentRequestDto request = defaultCommentRequest();
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(commentService.addComment(anyLong(), anyLong(), any())).thenReturn(response);
        mockMvc.perform(post(COMMENTS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("id", String.valueOf(bookId))
                .with(csrf())
                .with(user(userDetails))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is((int) response.getId())))
            .andExpect(jsonPath("$.body", is(response.getBody())));
    }

    @Test
    void addComment_userNotAuthenticated_shouldReturn401() throws Exception {
        long bookId = 10L;
        CommentRequestDto request = defaultCommentRequest();
        mockMvc.perform(post(COMMENTS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("id", String.valueOf(bookId))
                .with(csrf())
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void addComment_invalidBody_shouldReturn422() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        String invalidJson = "{invalid json}";
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        mockMvc.perform(
                post(COMMENTS)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidJson))
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void addComment_userNotFound_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentRequestDto request = defaultCommentRequest();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(commentService.addComment(anyLong(), anyLong(), any()))
            .thenThrow(new AppException(AppError.USER_NOT_FOUND));
        mockMvc.perform(
                post(COMMENTS)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void addComment_bookNotFound_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentRequestDto request = defaultCommentRequest();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(commentService.addComment(anyLong(), anyLong(), any()))
            .thenThrow(new AppException(AppError.BOOK_NOT_FOUND));
        mockMvc.perform(
                post(COMMENTS)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void updateComment_validRequest_shouldReturnSuccess() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentRequestDto request = defaultCommentRequest();
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(commentService.updateComment(anyLong(), anyLong(), anyLong(), any())).thenReturn(response);
        mockMvc.perform(put(COMMENTS + "/" + response.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("id", String.valueOf(bookId))
                .param("commentId", String.valueOf(response.getId()))
                .with(csrf())
                .with(user(userDetails))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is((int) response.getId())))
            .andExpect(jsonPath("$.body", is(response.getBody())));
    }

    @Test
    void updateComment_userNotAuthenticated_shouldReturn401() throws Exception {
        long bookId = 10L;
        CommentRequestDto request = defaultCommentRequest();
        CommentResponseDto response = defaultCommentResponse();
        when(commentService.updateComment(anyLong(), anyLong(), anyLong(), any())).thenReturn(response);
        mockMvc.perform(put(COMMENTS + "/" + response.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("id", String.valueOf(bookId))
                .with(csrf())
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void updateComment_invalidBody_shouldReturn422() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        String invalidJson = "{invalid json}";
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        mockMvc.perform(
                put(COMMENTS + "/" + response.getId())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidJson))
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateComment_userNotFound_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentRequestDto request = defaultCommentRequest();
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(commentService.updateComment(anyLong(), anyLong(), anyLong(), any()))
            .thenThrow(new AppException(AppError.USER_NOT_FOUND));
        mockMvc.perform(
                put(COMMENTS + "/" + response.getId())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void updateComment_commentNotFound_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentRequestDto request = defaultCommentRequest();
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(commentService.updateComment(anyLong(), anyLong(), anyLong(), any()))
            .thenThrow(new AppException(AppError.COMMENT_NOT_FOUND));
        mockMvc.perform(
                put(COMMENTS + "/" + response.getId())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void updateComment_commentNotAssociateWithUser_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentRequestDto request = defaultCommentRequest();
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(commentService.updateComment(anyLong(), anyLong(), anyLong(), any()))
            .thenThrow(new AppException(AppError.COMMENT_NOT_ASSOCIATED_WITH_USER));
        mockMvc.perform(
                put(COMMENTS + "/" + response.getId())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteComment_validRequest_shouldReturnNoContent() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        when(commentService.updateComment(anyLong(), anyLong(), anyLong(), any())).thenReturn(response);
        mockMvc.perform(delete(COMMENTS + "/" + response.getId())
                .param("id", String.valueOf(bookId))
                .param("commentId", String.valueOf(response.getId()))
                .with(csrf())
                .with(user(userDetails))
            )
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteComment_userNotAuthenticated_shouldReturn401() throws Exception {
        long bookId = 10L;
        CommentResponseDto response = defaultCommentResponse();
        when(commentService.updateComment(anyLong(), anyLong(), anyLong(), any())).thenReturn(response);
        mockMvc.perform(delete(COMMENTS + "/" + response.getId())
                .param("id", String.valueOf(bookId))
                .with(csrf())
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteComment_bookNotFound_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        doThrow(new AppException(AppError.BOOK_NOT_FOUND))
            .when(commentService)
            .deleteComment(anyLong(), anyLong(), anyLong());
        mockMvc.perform(
                delete(COMMENTS + "/" + response.getId())
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteComment_userNotFound_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        doThrow(new AppException(AppError.USER_NOT_FOUND))
            .when(commentService)
            .deleteComment(anyLong(), anyLong(), anyLong());
        mockMvc.perform(
                delete(COMMENTS + "/" + response.getId())
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteComment_commentNotFound_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        doThrow(new AppException(AppError.COMMENT_NOT_FOUND))
            .when(commentService)
            .deleteComment(anyLong(), anyLong(), anyLong());
        mockMvc.perform(
                delete(COMMENTS + "/" + response.getId())
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteComment_commentNotAssociateWithUser_shouldReturn404() throws Exception {
        long bookId = 10L;
        long userId = 100L;
        CommentResponseDto response = defaultCommentResponse();
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getRole()).thenReturn(UserRole.USER.name());
        mockSecurityContext(userDetails);
        doThrow(new AppException(AppError.COMMENT_NOT_ASSOCIATED_WITH_USER))
            .when(commentService)
            .deleteComment(anyLong(), anyLong(), anyLong());
        mockMvc.perform(
                delete(COMMENTS + "/" + response.getId())
                    .param("id", String.valueOf(bookId))
                    .with(csrf())
                    .with(user(userDetails))
            )
            .andExpect(status().isBadRequest());
    }
}
