package com.sun.librarymanagement.domain.controller;

import com.sun.librarymanagement.domain.dto.response.CategoryResponseDto;
import com.sun.librarymanagement.domain.service.CategoryService;
import com.sun.librarymanagement.utils.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.CATEGORIES)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable long id) {
        CategoryResponseDto response = categoryService.getCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<CategoryResponseDto> response = categoryService.getCategories();
        return ResponseEntity.ok(response);
    }
}
