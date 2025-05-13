package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.SearchBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.BookResponseDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.sun.librarymanagement.data.TestDataProvider.*;
import static com.sun.librarymanagement.utils.ApiPaths.BOOKS;
import static com.sun.librarymanagement.utils.Constant.PAGE_NUMBER_PARAM;
import static com.sun.librarymanagement.utils.Constant.PAGE_SIZE_PARAM;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

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
}
