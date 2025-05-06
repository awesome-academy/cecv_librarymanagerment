package com.sun.librarymanagement.data;

import com.sun.librarymanagement.domain.dto.request.CommentRequestDto;
import com.sun.librarymanagement.domain.dto.request.SearchBookRequestDto;
import com.sun.librarymanagement.domain.dto.response.*;
import com.sun.librarymanagement.domain.entity.*;
import com.sun.librarymanagement.domain.model.UserRole;
import com.sun.librarymanagement.security.AppUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

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

    public static PaginatedResponseDto<CommentResponseDto> defaultPaginatedCommentResponse() {
        return new PaginatedResponseDto<>(List.of(defaultCommentResponse()), 0L, 1, 1L);
    }

    public static CommentResponseDto defaultCommentResponse() {
        return new CommentResponseDto(1L, "Harper Voyager");
    }

    public static CommentRequestDto defaultCommentRequest() {
        return new CommentRequestDto("Harper Voyager");
    }

    public static CommentEntity defaultCommentEntity() {
        return new CommentEntity("Harper Voyager", defaultUserEntity(), defaultBookEntity());
    }
}
