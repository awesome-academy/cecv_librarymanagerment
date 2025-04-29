package com.sun.librarymanagement.data;

import com.sun.librarymanagement.domain.dto.request.SearchBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.*;
import com.sun.librarymanagement.domain.entity.AuthorEntity;
import com.sun.librarymanagement.domain.entity.BookEntity;
import com.sun.librarymanagement.domain.entity.PublisherEntity;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.model.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TestDataProvider {

    public static int DEFAULT_PAGE_NUMBER = 0;
    public static int DEFAULT_PAGE_SIZE = 10;

    public static BookResponseDto defaultBookResponse() {
        return new BookResponseDto(
            1L,
            "The Artist's Way: A Spiritual Path to Higher Creativity",
            "The Artist's Way is the seminal book on the subject of creativity.",
            "https://i.thriftbooks.com/api/imagehandler/m/806BDDCC87DA4EBB29D42AB58DD36A849CEB0B9E.jpeg",
            defaultPublisherResponse(),
            1,
            1,
            defaultAuthorResponse(),
            Set.of(defaultCategoryResponse()),
            false
        );
    }

    public static PublisherResponseDto defaultPublisherResponse() {
        return new PublisherResponseDto(1L, "Harper Voyager");
    }

    public static AuthorResponseDto defaultAuthorResponse() {
        return new AuthorResponseDto(1L, "Luis Prats");
    }

    public static CategoryResponseDto defaultCategoryResponse() {
        return new CategoryResponseDto(1L, "Biographies");
    }

    public static PaginatedResponseDto<BookResponseDto> defaultPaginatedBookResponse() {
        return new PaginatedResponseDto<>(List.of(defaultBookResponse()), 0L, 1, 1L);
    }

    public static SearchBookRequestDto defaultSearchBookRequest() {
        return new SearchBookRequestDto(
            "Harper Voyager",
            "Biographies",
            "Luis Prats",
            "The Artist's Way",
            "Creativity"
        );
    }

    public static UserEntity defaultUserEntity() {
        return new UserEntity(
            1L,
            "Example",
            "example@gmail.com",
            "password",
            UserRole.USER,
            "token"
        );
    }

    public static BookEntity defaultBookEntity() {
        return new BookEntity(
            "The Artist's Way: A Spiritual Path to Higher Creativity",
            "The Artist's Way is the seminal book on the subject of creativity.",
            "https://i.thriftbooks.com/api/imagehandler/m/806BDDCC87DA4EBB29D42AB58DD36A849CEB0B9E.jpeg",
            defaultPublisherEntity(),
            1,
            1,
            defaultAuthorEntity(),
            null,
            LocalDateTime.now(),
            null
        );
    }

    public static PublisherEntity defaultPublisherEntity() {
        return new PublisherEntity("Harper Voyager");
    }

    public static AuthorEntity defaultAuthorEntity() {
        return new AuthorEntity("Luis Prats", "Bio", LocalDateTime.now());
    }
}
