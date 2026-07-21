package com.blog.blogserverjava.service.blog;

import com.blog.blogserverjava.dto.category.AddCategoryRequest;
import com.blog.blogserverjava.dto.category.CategoryListRequest;
import com.blog.blogserverjava.dto.category.CategoryListResponse;
import com.blog.blogserverjava.dto.category.UpdateCategoryRequest;

public interface CategoryService {
    CategoryListResponse getCategoryList(CategoryListRequest request);

    void addCategory(AddCategoryRequest request);

    void updateCategory(UpdateCategoryRequest request);

    void delCategory(Integer id);
}
