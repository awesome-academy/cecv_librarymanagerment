package com.sun.librarymanagement.service;

import com.sun.librarymanagement.domain.dto.request.PublisherRequestDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublishersResponseDto;
import com.sun.librarymanagement.domain.entity.PublisherEntity;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import com.sun.librarymanagement.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

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
        PublisherEntity publisherEntities = getPublisherEntityById(id);
        return new PublisherResponseDto(publisherEntities.getId(), publisherEntities.getName());
    }

    public PublisherResponseDto updatePublisher(long id, PublisherRequestDto publisherRequestDto) {
        checkIfPublisherExists(publisherRequestDto.getName());
        PublisherEntity currentPublisherEntity = getPublisherEntityById(id);
        currentPublisherEntity.setName(publisherRequestDto.getName());
        PublisherEntity result = publisherRepository.save(currentPublisherEntity);
        return new PublisherResponseDto(result.getId(), result.getName());
    }

    public void deletePublisher(long id) {
        PublisherEntity currentPublisherEntity = getPublisherEntityById(id);
        publisherRepository.delete(currentPublisherEntity);
    }

    private PublisherEntity getPublisherEntityById(long id) {
        return publisherRepository.findById(id).orElseThrow(
                () -> new AppException(AppError.PUBLISHER_NOT_FOUND)
        );
    }

    private void checkIfPublisherExists(String publisherName) {
        boolean isExist = publisherRepository.existsByName(publisherName);
        if (isExist) {
            throw new AppException(AppError.NAME_TAKEN);
        }
    }
}
