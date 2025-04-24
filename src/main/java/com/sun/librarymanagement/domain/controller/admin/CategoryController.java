package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.request.CategoryRequestDto;
import com.sun.librarymanagement.domain.dto.response.CategoryResponseDto;
import com.sun.librarymanagement.domain.service.CategoryService;
import com.sun.librarymanagement.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController("adminCategoryController")
@RequestMapping(ApiPaths.CATEGORIES_ADMIN)
@AllArgsConstructor()
public class CategoryController extends AdminController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody @Valid CategoryRequestDto request) {
        CategoryResponseDto response = categoryService.addCategory(request);
        URI location = URI.create(ApiPaths.CATEGORIES + "/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable long id, @RequestBody @Valid CategoryRequestDto request) {
        CategoryResponseDto response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
