package com.blog.blogserverjava.controller.blog;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.dto.category.AddCategoryRequest;
import com.blog.blogserverjava.dto.category.CategoryListRequest;
import com.blog.blogserverjava.dto.category.CategoryListResponse;
import com.blog.blogserverjava.dto.category.UpdateCategoryRequest;
import com.blog.blogserverjava.service.blog.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public Result<CategoryListResponse> getCategoryList(CategoryListRequest request) {
        return Result.success(categoryService.getCategoryList(request));
    }

    @PostMapping("/addCategory")
    public Result<Void> addCategory(@Valid @RequestBody AddCategoryRequest request) {
        categoryService.addCategory(request);
        return Result.success(null);
    }

    @PutMapping("/updateCategory")
    public Result<Void> updateCategory(@Valid @RequestBody UpdateCategoryRequest request) {
        categoryService.updateCategory(request);
        return Result.success(null);
    }

    @DeleteMapping("/delCategory")
    public Result<Void> delCategory(@RequestParam Integer id) {
        categoryService.delCategory(id);
        return Result.success(null);
    }
}
