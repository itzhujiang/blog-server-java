package com.blog.blogserverjava.dto.category;

import lombok.Data;

@Data
public class CategoryListRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String name;
}
