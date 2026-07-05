package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.ArtworkMediaUsage;
import lombok.Data;

@Data
@TableName("artwork_media")
public class ArtworkMedia {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** AI作品ID */
    private Integer artworkId;
    /** 媒体文件ID */
    private Integer mediaId;
    /** 使用类型 */
    private ArtworkMediaUsage usageType;
    /** 排序权重 */
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
}
