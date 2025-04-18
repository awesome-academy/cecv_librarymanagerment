package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.dto.response.PublishersResponseDto;
import com.sun.librarymanagement.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @GetMapping
    public ResponseEntity<PublishersResponseDto> getPublishers(
            @RequestParam(defaultValue = "0", name = "page_number") int pageNumber,
            @RequestParam(defaultValue = "10", name = "page_size") int pageSize) {
        return ResponseEntity.ok().body(publisherService.getPublishers(pageNumber, pageSize));

    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> getPublisher(@PathVariable Long id) {
        return ResponseEntity.ok().body(publisherService.getPublisher(id));
    }
}
