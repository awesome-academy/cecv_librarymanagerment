package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.PublisherRequestDto;
import com.sun.librarymanagement.domain.dto.response.PublisherResponseDto;
import com.sun.librarymanagement.domain.service.PublisherService;
import com.sun.librarymanagement.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController("adminPublisherController")
@RequestMapping(ApiPaths.PUBLISHERS_ADMIN)
@AllArgsConstructor()
public class PublisherController extends AdminController {

    private final PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponseDto> addPublisher(
        @RequestBody @Valid PublisherRequestDto publisher
    ) {
        PublisherResponseDto response = publisherService.addPublisher(publisher);
        URI location = URI.create(ApiPaths.PUBLISHERS + "/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
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
