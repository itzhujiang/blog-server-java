package com.blog.blogserverjava.dto.article;

import com.blog.blogserverjava.enums.ArticleStatus;
import lombok.Data;

@Data
public class ArticleListRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String title;
    private ArticleStatus status;
    private Long publishedAtStart;
    private Long publishedAtEnd;
    private Integer categoryId;
    /** asc / desc */
    private String viewCountSort;
}
