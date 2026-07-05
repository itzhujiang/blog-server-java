package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.ArtworkStatus;
import lombok.Data;

@Data
@TableName("ai_artworks")
public class AiArtwork {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 作品标题 */
    private String title;
    /** URL标识 */
    private String slug;
    /** 作品描述 */
    private String description;
    /** 作品分类 */
    private String category;
    /** AI生成提示词 */
    private String creationPrompt;
    /** 使用的AI模型 */
    private String aiModel;
    /** 浏览次数 */
    private Integer viewCount;
    /** 点赞数 */
    private Integer likeCount;
    /** 是否推荐 */
    private Boolean isFeatured;
    /** 排序权重 */
    private Integer sortOrder;
    /** 发布状态 */
    private ArtworkStatus status;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
