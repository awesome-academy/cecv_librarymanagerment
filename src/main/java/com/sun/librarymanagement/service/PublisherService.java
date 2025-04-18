package com.sun.librarymanagement.service;

import com.sun.librarymanagement.domain.dto.request.PublisherRequestDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublishersResponseDto;
import com.sun.librarymanagement.domain.entity.PublisherEntity;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.domain.repository.PublisherRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    private final EntityManager entityManager;

    public PublisherResponseDto addPublisher(PublisherRequestDto publisherRequestDto) {
        checkIfPublisherExists(publisherRequestDto.getName());
        PublisherEntity result = publisherRepository.save(
            new PublisherEntity(publisherRequestDto.getName()));
        return new PublisherResponseDto(result.getId(), result.getName());
    }

    public PublishersResponseDto getPublishers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PublisherEntity> publisherEntities = publisherRepository.findAll(pageable);
        return new PublishersResponseDto(publisherEntities.stream().map(
            (e) -> new PublisherResponseDto(e.getId(), e.getName())
        ).toList(),
            pageNumber,
            publisherEntities.getTotalPages(),
            publisherEntities.getTotalElements()
        );
    }

    public PublisherResponseDto getPublisher(long id) {
        PublisherEntity publisherEntities = publisherRepository.findById(id).orElseThrow(
            () -> new AppException(AppError.PUBLISHER_NOT_FOUND)
        );
        return new PublisherResponseDto(publisherEntities.getId(), publisherEntities.getName());
    }

    @Transactional
    public PublisherResponseDto updatePublisher(long id, PublisherRequestDto publisherRequestDto) {
        checkIfPublisherExists(publisherRequestDto.getName());
        PublisherEntity currentPublisherEntity = getPublisherEntityByIdWithLock(id);
        currentPublisherEntity.setName(publisherRequestDto.getName());
        PublisherEntity result = publisherRepository.save(currentPublisherEntity);
        return new PublisherResponseDto(result.getId(), result.getName());
    }

    @Transactional
    public void deletePublisher(long id) {
        PublisherEntity currentPublisherEntity = getPublisherEntityByIdWithLock(id);
        publisherRepository.delete(currentPublisherEntity);
    }

    private PublisherEntity getPublisherEntityByIdWithLock(long id) {
        return Optional.ofNullable(
            entityManager.find(PublisherEntity.class, id, LockModeType.PESSIMISTIC_WRITE)).orElseThrow(
            () -> new AppException(AppError.PUBLISHER_NOT_FOUND
            )
        );
    }

    private void checkIfPublisherExists(String publisherName) {
        boolean isExist = publisherRepository.existsByName(publisherName);
        if (isExist) {
            throw new AppException(AppError.PUBLISHER_ALREADY_EXISTS);
        }
    }
}
