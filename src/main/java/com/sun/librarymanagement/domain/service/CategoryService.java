package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.CategoryRequestDto;
import com.sun.librarymanagement.domain.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryResponseDto addCategory(CategoryRequestDto request);

    CategoryResponseDto getCategory(long id);

    List<CategoryResponseDto> getCategories();

    CategoryResponseDto updateCategory(long id, CategoryRequestDto request);

    void deleteCategory(long id);
}
