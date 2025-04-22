package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.AuthorRequestDto;
import com.sun.librarymanagement.domain.dto.response.AuthorResponseDto;
import com.sun.librarymanagement.domain.dto.response.AuthorsResponseDto;
import com.sun.librarymanagement.domain.entity.AuthorEntity;
import com.sun.librarymanagement.domain.model.FollowInfo;
import com.sun.librarymanagement.domain.model.FollowType;
import com.sun.librarymanagement.domain.repository.AuthorRepository;
import com.sun.librarymanagement.domain.repository.FollowRepository;
import com.sun.librarymanagement.domain.service.AuthorService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.security.AppUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final FollowRepository followRepository;

    @Override
    public AuthorResponseDto addAuthor(AuthorRequestDto authorRequestDto) {
        checkIfAuthorExists(authorRequestDto.getName());
        AuthorEntity result = authorRepository.save(
            new AuthorEntity(
                authorRequestDto.getName(),
                authorRequestDto.getBio(),
                authorRequestDto.getDateOfBirth()
            )
        );
        return new AuthorResponseDto(
            result.getId(),
            result.getName(),
            result.getBio(),
            result.getDateOfBirth()
        );
    }

    @Override
    public AuthorsResponseDto getAuthors(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<AuthorEntity> authorEntities = authorRepository.findAll(pageable);
        return new AuthorsResponseDto(authorEntities.stream().map(
            (e) -> new AuthorResponseDto(
                e.getId(),
                e.getName()
            )
        ).toList(),
            pageNumber,
            authorEntities.getTotalPages(),
            authorEntities.getTotalElements()
        );
    }

    @Override
    public AuthorResponseDto getAuthor(long id, AppUserDetails currentUser) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(
            () -> new AppException(AppError.AUTHOR_NOT_FOUND)
        );
        boolean isFollowing = currentUser != null && followRepository.findById(
            new FollowInfo(currentUser.getId(), authorEntity.getId(), FollowType.AUTHOR)
        ).isPresent();
        return new AuthorResponseDto(
            authorEntity.getId(),
            authorEntity.getName(),
            authorEntity.getBio(),
            authorEntity.getDateOfBirth(),
            isFollowing
        );
    }

    @Override
    @Transactional
    public AuthorResponseDto updateAuthor(long id, AuthorRequestDto authorRequestDto) {
        checkIfAuthorExists(authorRequestDto.getName());
        AuthorEntity currentAuthorEntity = getAuthorEntityByIdWithLock(id);
        currentAuthorEntity.setName(authorRequestDto.getName());
        currentAuthorEntity.setBio(authorRequestDto.getBio());
        currentAuthorEntity.setDateOfBirth(authorRequestDto.getDateOfBirth());
        AuthorEntity result = authorRepository.save(currentAuthorEntity);
        return new AuthorResponseDto(
            result.getId(),
            result.getName(),
            result.getBio(),
            result.getDateOfBirth()
        );
    }

    @Override
    @Transactional
    public void deleteAuthor(long id) {
        AuthorEntity currentAuthorEntity = getAuthorEntityByIdWithLock(id);
        authorRepository.delete(currentAuthorEntity);
    }

    private AuthorEntity getAuthorEntityByIdWithLock(long id) {
        return authorRepository.findByIdWithLock(id).orElseThrow(
            () -> new AppException(AppError.AUTHOR_NOT_FOUND)
        );
    }

    private void checkIfAuthorExists(String publisherName) {
        boolean isExist = authorRepository.existsByName(publisherName);
        if (isExist) {
            throw new AppException(AppError.AUTHOR_ALREADY_EXISTS);
        }
    }
}
