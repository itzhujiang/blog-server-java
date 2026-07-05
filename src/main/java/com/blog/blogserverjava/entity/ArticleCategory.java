package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("article_categories")
public class ArticleCategory {

    /** 文章ID */
    private Integer articleId;
    /** 分类ID */
    private Integer categoryId;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
}
