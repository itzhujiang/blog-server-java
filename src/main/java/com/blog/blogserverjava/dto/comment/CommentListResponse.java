package com.blog.blogserverjava.dto.comment;

import com.blog.blogserverjava.enums.CommentStatus;

import java.util.List;

public record CommentListResponse(List<CommentItem> data, Pagination pagination) {

    public record CommentItem(
            Integer id,
            Integer articleId,
            Integer parentId,
            String authorName,
            String authorEmail,
            String content,
            CommentStatus status,
            Integer likeCount,
            Long createdAt
    ) {
    }

    public record Pagination(Integer page, Integer size, Long total) {
    }
}
