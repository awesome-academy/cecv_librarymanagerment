package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublishersResponseDto;
import com.sun.librarymanagement.domain.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<PublishersResponseDto> getPublishers(
        @RequestParam(defaultValue = "0", name = "page_number") int pageNumber,
        @RequestParam(defaultValue = "10", name = "page_size") int pageSize
    ) {
        return ResponseEntity.ok(publisherService.getPublishers(pageNumber, pageSize));

    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> getPublisher(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisher(id));
    }
}
