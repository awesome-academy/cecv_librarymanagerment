package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.PublisherRequestDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublishersResponseDto;
import com.sun.librarymanagement.security.AppUserDetails;

public interface PublisherService {

    PublisherResponseDto addPublisher(PublisherRequestDto publisherRequestDto);

    PublishersResponseDto getPublishers(int pageNumber, int pageSize);

    PublisherResponseDto getPublisher(long id, AppUserDetails currentUser);

    PublisherResponseDto updatePublisher(long id, PublisherRequestDto publisherRequestDto);

    void deletePublisher(long id);
}
