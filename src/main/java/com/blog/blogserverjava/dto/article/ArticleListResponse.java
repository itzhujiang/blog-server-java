package com.blog.blogserverjava.dto.article;

import com.blog.blogserverjava.enums.ArticleStatus;

import java.util.List;

public record ArticleListResponse(List<ArticleItem> data, Pagination pagination) {

    public record ArticleItem(
            Integer id,
            String title,
            String slug,
            String excerpt,
            String content,
            String thumbnailUrl,
            List<String> attachmentUrlArr,
            String authorName,
            Integer readingTime,
            Integer viewCount,
            ArticleStatus status,
            Long publishedAt,
            List<CategoryBrief> categories
    ) {
    }

    public record CategoryBrief(Integer id, String name, String slug) {
    }

    public record Pagination(Integer page, Integer size, Long total) {
    }
}
