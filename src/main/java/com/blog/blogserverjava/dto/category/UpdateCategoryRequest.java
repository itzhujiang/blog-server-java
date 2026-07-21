package com.blog.blogserverjava.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCategoryRequest {

    @NotNull(message = "分类ID不能为空")
    private Integer id;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    @NotBlank(message = "URL标识不能为空")
    private String slug;
}
