package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.PublisherRequestDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.service.PublisherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminPublisherController")
@RequestMapping("/api/v1/admin/publishers")
@AllArgsConstructor()
public class PublisherController extends AdminController {

    private final PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponseDto> addPublisher(
        @RequestBody @Valid PublisherRequestDto publisher
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(publisherService.addPublisher(publisher));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> updatePublisher(
        @PathVariable Long id,
        @Valid @RequestBody PublisherRequestDto publisher
    ) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, publisher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
