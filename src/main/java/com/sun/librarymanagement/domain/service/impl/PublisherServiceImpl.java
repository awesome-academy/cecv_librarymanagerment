package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.PublisherRequestDto;
import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.entity.PublisherEntity;
import com.sun.librarymanagement.domain.model.FollowInfo;
import com.sun.librarymanagement.domain.model.FollowType;
import com.sun.librarymanagement.domain.repository.FollowRepository;
import com.sun.librarymanagement.domain.repository.PublisherRepository;
import com.sun.librarymanagement.domain.service.PublisherService;
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
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    private final FollowRepository followRepository;

    @Override
    public PublisherResponseDto addPublisher(PublisherRequestDto publisherRequestDto) {
        checkIfPublisherExists(publisherRequestDto.getName());
        PublisherEntity result = publisherRepository.save(
            new PublisherEntity(publisherRequestDto.getName()));
        return new PublisherResponseDto(result.getId(), result.getName());
    }

    @Override
    public PaginatedResponseDto<PublisherResponseDto> getPublishers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PublisherEntity> publisherEntities = publisherRepository.findAll(pageable);
        return new PaginatedResponseDto<>(publisherEntities.stream().map(
            (e) -> new PublisherResponseDto(e.getId(), e.getName())
        ).toList(),
            pageNumber,
            publisherEntities.getTotalPages(),
            publisherEntities.getTotalElements()
        );
    }

    @Override
    public PublisherResponseDto getPublisher(long id, AppUserDetails currentUser) {
        PublisherEntity publisherEntities = publisherRepository.findById(id).orElseThrow(
            () -> new AppException(AppError.PUBLISHER_NOT_FOUND)
        );
        boolean isFollowing = currentUser != null && followRepository.findById(
            new FollowInfo(currentUser.getId(), publisherEntities.getId(), FollowType.PUBLISHER)
        ).isPresent();
        return new PublisherResponseDto(
            publisherEntities.getId(),
            publisherEntities.getName(),
            isFollowing
        );
    }

    @Override
    @Transactional
    public PublisherResponseDto updatePublisher(long id, PublisherRequestDto publisherRequestDto) {
        checkIfPublisherExists(publisherRequestDto.getName());
        PublisherEntity currentPublisherEntity = getPublisherEntityByIdWithLock(id);
        currentPublisherEntity.setName(publisherRequestDto.getName());
        PublisherEntity result = publisherRepository.save(currentPublisherEntity);
        return new PublisherResponseDto(result.getId(), result.getName());
    }

    @Override
    @Transactional
    public void deletePublisher(long id) {
        PublisherEntity currentPublisherEntity = getPublisherEntityByIdWithLock(id);
        publisherRepository.delete(currentPublisherEntity);
    }

    private PublisherEntity getPublisherEntityByIdWithLock(long id) {
        return publisherRepository.findByIdWithLock(id).orElseThrow(
            () -> new AppException(AppError.PUBLISHER_NOT_FOUND)
        );
    }

    private void checkIfPublisherExists(String publisherName) {
        boolean isExist = publisherRepository.existsByName(publisherName);
        if (isExist) {
            throw new AppException(AppError.PUBLISHER_ALREADY_EXISTS);
        }
    }
}
