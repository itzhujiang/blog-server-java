package com.blog.blogserverjava.dto.category;

import java.util.List;

public record CategoryListResponse(List<CategoryItem> data, Pagination pagination) {

    public record CategoryItem(Integer id, String name, String slug, Long createdAt, Long updatedAt) {
    }

    public record Pagination(Integer page, Integer size, Long total) {
    }
}
