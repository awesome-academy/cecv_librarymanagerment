package com.sun.librarymanagement.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.librarymanagement.domain.dto.request.SearchBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.service.BookService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.security.JwtAuthFilter;
import com.sun.librarymanagement.security.WebSecurityConfiguration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.sun.librarymanagement.data.TestDataProvider.*;
import static com.sun.librarymanagement.utils.ApiPaths.BOOKS;
import static com.sun.librarymanagement.utils.Constant.PAGE_NUMBER_PARAM;
import static com.sun.librarymanagement.utils.Constant.PAGE_SIZE_PARAM;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
@Import(WebSecurityConfiguration.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @BeforeEach
    void setUp() {
        doAnswer(invocation -> {
            HttpServletRequest httpServletRequest = invocation.getArgument(0, HttpServletRequest.class);
            HttpServletResponse httpServletResponse = invocation.getArgument(1, HttpServletResponse.class);
            FilterChain chain = invocation.getArgument(2, FilterChain.class);
            chain.doFilter(httpServletRequest, httpServletResponse);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());
    }

    @Test
    void search_success_with_all_params() throws Exception {
        PaginatedResponseDto<BookResponseDto> response = defaultPaginatedBookResponse();
        SearchBookRequestDto request = defaultSearchBookRequest();
        when(
            bookService.search(
                any(),
                eq(DEFAULT_PAGE_NUMBER),
                eq(DEFAULT_PAGE_SIZE)
            )
        ).thenReturn(response);
        mockMvc.perform(
                post(BOOKS + "/search")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
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
            .andExpect(jsonPath("$.results[0].name", is(response.getResults().get(0).getName())));
    }

    @Test
    void search_success_with_default_params() throws Exception {
        PaginatedResponseDto<BookResponseDto> response = defaultPaginatedBookResponse();
        SearchBookRequestDto request = defaultSearchBookRequest();
        when(
            bookService.search(
                any(),
                eq(DEFAULT_PAGE_NUMBER),
                eq(DEFAULT_PAGE_SIZE)
            )
        ).thenReturn(response);
        mockMvc.perform(
                post(BOOKS + "/search")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.page", is((int) response.getPage())))
            .andExpect(jsonPath("$.total_pages", is((int) response.getTotalPages())))
            .andExpect(jsonPath("$.total_results", is((int) response.getTotalResults())))
            .andExpect(jsonPath("$.results", hasSize(1)))
            .andExpect(jsonPath("$.results[0].id", is((int) response.getResults().get(0).getId())))
            .andExpect(jsonPath("$.results[0].name", is(response.getResults().get(0).getName())));
    }

    @Test
    void search_invalid_body_should_return_422() throws Exception {
        String invalidJson = "{invalid json}";
        mockMvc.perform(
                post(BOOKS + "/search")
                    .contentType(APPLICATION_JSON)
                    .content(invalidJson)
            )
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void search_edge_case_invalid_page_params() throws Exception {
        PaginatedResponseDto<BookResponseDto> response = defaultPaginatedBookResponse();
        SearchBookRequestDto request = defaultSearchBookRequest();
        when(
            bookService.search(
                any(),
                eq(-1),
                eq(-1)
            )
        ).thenReturn(response);
        mockMvc.perform(
                post(BOOKS + "/search")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .param(PAGE_NUMBER_PARAM, "-1")
                    .param(PAGE_SIZE_PARAM, "-1")
            )
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void favorite_success() throws Exception {
        BookResponseDto response = defaultBookResponse();
        long bookId = 1L;
        long userId = 100L;
        AppUserDetails userDetails = Mockito.mock(AppUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);
        mockSecurityContext(userDetails);
        when(bookService.favorite(bookId, userId)).thenReturn(response);
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
        mockSecurityContext(userDetails);
        when(bookService.favorite(anyLong(), anyLong()))
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
        mockSecurityContext(userDetails);
        when(bookService.favorite(anyLong(), anyLong()))
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
        mockSecurityContext(userDetails);
        Mockito.doNothing().when(bookService).unfavorite(bookId, userId);
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
            .when(bookService)
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

    @SneakyThrows
    private void mockSecurityContext(AppUserDetails userDetails) {
        doAnswer(invocation -> {
            HttpServletRequest httpServletRequest = invocation.getArgument(0, HttpServletRequest.class);
            HttpServletResponse httpServletResponse = invocation.getArgument(1, HttpServletResponse.class);
            FilterChain chain = invocation.getArgument(2, FilterChain.class);
            chain.doFilter(httpServletRequest, httpServletResponse);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());
    }
}
