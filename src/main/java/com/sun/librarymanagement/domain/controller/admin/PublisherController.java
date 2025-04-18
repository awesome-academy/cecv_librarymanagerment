package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.PublisherRequestDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminPublisherController")
@RequestMapping("/api/v1/admin/publishers")
public class PublisherController extends AdminController {

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
