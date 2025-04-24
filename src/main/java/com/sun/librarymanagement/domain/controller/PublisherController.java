package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.service.PublisherService;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.PUBLISHERS)
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<PublisherResponseDto>> getPublishers(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        return ResponseEntity.ok(publisherService.getPublishers(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> getPublisher(
        @PathVariable Long id,
        @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return ResponseEntity.ok(publisherService.getPublisher(id, currentUser));
    }
}
