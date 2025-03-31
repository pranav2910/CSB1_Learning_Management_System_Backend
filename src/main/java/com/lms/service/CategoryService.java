package com.lms.service;

import com.lms.dto.request.CategoryRequest;
import com.lms.dto.response.CategoryResponse;
import com.lms.dto.response.CourseResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategories();
    List<CourseResponse> getCoursesByCategory(String categoryId);
    void deleteCategory(String categoryId);
}