package com.lms.service.impl;

import com.lms.dto.request.CategoryRequest;
import com.lms.dto.response.CategoryResponse;
import com.lms.dto.response.CourseResponse;
import com.lms.model.Category;
import com.lms.repository.CategoryRepository;
import com.lms.repository.CourseRepository;
import com.lms.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .iconUrl(request.getIconUrl())
                .courseCount(0)
                .build();
        
        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesByCategory(String categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(course -> CourseResponse.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(String categoryId) {
        if (courseRepository.existsByCategoryId(categoryId)) {
            throw new IllegalStateException("Cannot delete category with existing courses");
        }
        categoryRepository.deleteById(categoryId);
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .iconUrl(category.getIconUrl())
                .courseCount(category.getCourseCount())
                .build();
    }
}