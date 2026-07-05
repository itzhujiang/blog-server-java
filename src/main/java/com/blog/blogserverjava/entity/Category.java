package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("categories")
public class Category {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 分类名称 */
    private String name;
    /** URL标识 */
    private String slug;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;

    @TableLogic
    private Long deletedAt;
}
