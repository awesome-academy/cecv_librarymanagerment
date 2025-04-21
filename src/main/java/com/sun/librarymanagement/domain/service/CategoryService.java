package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.dto.request.CategoryRequest;
import com.sun.librarymanagement.domain.dto.response.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryResponse addCategory(CategoryRequest request);

    CategoryResponse getCategory(long id);

    List<CategoryResponse> getCategories();

    CategoryResponse updateCategory(long id, CategoryRequest request);

    void deleteCategory(long id);
}
