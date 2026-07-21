package com.blog.blogserverjava.service.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.dto.category.AddCategoryRequest;
import com.blog.blogserverjava.dto.category.CategoryListRequest;
import com.blog.blogserverjava.dto.category.CategoryListResponse;
import com.blog.blogserverjava.dto.category.UpdateCategoryRequest;
import com.blog.blogserverjava.entity.ArticleCategory;
import com.blog.blogserverjava.entity.Category;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.ArticleCategoryMapper;
import com.blog.blogserverjava.mapper.CategoryMapper;
import com.blog.blogserverjava.service.blog.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final ArticleCategoryMapper articleCategoryMapper;

    @Override
    public CategoryListResponse getCategoryList(CategoryListRequest request) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>()
                .like(StringUtils.hasText(request.getName()), Category::getName, request.getName())
                .orderByAsc(Category::getId);

        Page<Category> page = categoryMapper.selectPage(
                new Page<>(request.getPage(), request.getSize()), wrapper
        );

        List<CategoryListResponse.CategoryItem> items = page.getRecords().stream()
                .map(c -> new CategoryListResponse.CategoryItem(
                        c.getId(), c.getName(), c.getSlug(), c.getCreatedAt(), c.getUpdatedAt()))
                .toList();

        return new CategoryListResponse(
                items,
                new CategoryListResponse.Pagination(request.getPage(), request.getSize(), page.getTotal())
        );
    }

    @Override
    public void addCategory(AddCategoryRequest request) {
        Category existing = categoryMapper.selectOne(
                new LambdaQueryWrapper<Category>().eq(Category::getSlug, request.getSlug())
        );
        if (existing != null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "分类URL标识已存在");
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(UpdateCategoryRequest request) {
        Category category = categoryMapper.selectById(request.getId());
        if (category == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "分类ID不存在");
        }
        Category existing = categoryMapper.selectOne(
                new LambdaQueryWrapper<Category>().eq(Category::getSlug, request.getSlug())
        );
        if (existing != null && !existing.getId().equals(request.getId())) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "分类URL标识已存在");
        }
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        categoryMapper.updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCategory(Integer id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "分类不存在，无法删除");
        }
        // 软删除分类
        categoryMapper.deleteById(id);
        // 物理删除关联记录
        articleCategoryMapper.delete(
                new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getCategoryId, id)
        );
    }
}
