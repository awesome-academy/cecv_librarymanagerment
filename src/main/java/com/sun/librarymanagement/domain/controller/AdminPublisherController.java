package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.request.PublisherRequestDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/publishers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponseDto> addPublisher(
            @RequestBody @Valid PublisherRequestDto publisherRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.addPublisher(publisherRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> updatePublisher(
            @PathVariable Long id,
            @Valid @RequestBody PublisherRequestDto publisherRequestDto) {
        return ResponseEntity.ok().body(publisherService.updatePublisher(id, publisherRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
