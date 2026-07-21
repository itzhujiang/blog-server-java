package com.blog.blogserverjava.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCategoryRequest {

    @NotBlank(message = "分类名称不能为空")
    private String name;

    @NotBlank(message = "URL标识不能为空")
    private String slug;
}
