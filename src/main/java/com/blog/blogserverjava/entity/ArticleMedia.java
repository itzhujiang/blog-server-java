package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.ArticleMediaUsage;
import lombok.Data;

@Data
@TableName("article_media")
public class ArticleMedia {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 文章ID */
    private Integer articleId;
    /** 媒体文件ID */
    private Integer mediaId;
    /** 使用类型 */
    private ArticleMediaUsage usageType;
    /** 排序权重 */
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
}
