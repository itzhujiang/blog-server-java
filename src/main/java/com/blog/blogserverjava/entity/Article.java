package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.ArticleStatus;
import lombok.Data;

@Data
@TableName("articles")
public class Article {
    /** 文章ID */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 标题 */
    private String title;
    /** URL友好标识 */
    private String slug;
    /** 文章正文内容（Markdown） */
    private String content;
    /** 文章摘要 */
    private String excerpt;
    /** 作者名 */
    private String authorName;
    /** 预计阅读时间(分钟) */
    private Integer readingTime;
    /** 浏览次数 */
    private Integer viewCount;
    /** 发布状态，draft=草稿, published=已发布, archived=已归档 */
    private ArticleStatus status;
    /** 发布时间（毫秒级Unix时间戳） */
    private Long publishedAt;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
    /** 删除字段 */
    @TableLogic
    private Long deletedAt;
}
