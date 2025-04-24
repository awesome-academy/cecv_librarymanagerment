package com.sun.librarymanagement.domain.service.impl;

import com.sun.librarymanagement.domain.dto.request.CategoryRequestDto;
import com.sun.librarymanagement.domain.dto.response.CategoryResponseDto;
import com.sun.librarymanagement.domain.entity.CategoryEntity;
import com.sun.librarymanagement.domain.repository.CategoryRepository;
import com.sun.librarymanagement.domain.service.CategoryService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto addCategory(@NotNull CategoryRequestDto request) {
        categoryRepository.findByName(request.getName()).ifPresent(value -> {
            throw new AppException(AppError.CATEGORY_ALREADY_EXISTS);
        });
        CategoryEntity entity = categoryRepository.save(new CategoryEntity(request.getName()));
        return new CategoryResponseDto(entity.getId(), entity.getName());
    }

    @Override
    public CategoryResponseDto getCategory(long id) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() -> new AppException(AppError.CATEGORY_NOT_FOUND));
        return new CategoryResponseDto(entity.getId(), entity.getName());
    }

    @Override
    public List<CategoryResponseDto> getCategories() {
        List<CategoryEntity> entities = categoryRepository.findAll();
        return entities.stream().map(entity -> new CategoryResponseDto(entity.getId(), entity.getName())).toList();
    }

    @Override
    public CategoryResponseDto updateCategory(long id, @NotNull CategoryRequestDto request) {
        CategoryEntity currentEntity = categoryRepository.findById(id).orElseThrow(() -> new AppException(AppError.CATEGORY_NOT_FOUND));
        categoryRepository.findByName(request.getName()).ifPresent(value -> {
            throw new AppException(AppError.CATEGORY_ALREADY_EXISTS);
        });
        currentEntity.setName(request.getName());
        CategoryEntity entity = categoryRepository.save(currentEntity);
        return new CategoryResponseDto(entity.getId(), entity.getName());
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.findById(id).orElseThrow(() -> new AppException(AppError.CATEGORY_NOT_FOUND));
        categoryRepository.deleteById(id);
    }
}
