package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.AboutPageMediaUsage;
import lombok.Data;

@Data
@TableName("about_page_media")
public class AboutPageMedia {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 关于我页面ID */
    private Integer aboutPageId;
    /** 媒体文件ID */
    private Integer mediaId;
    /** 使用类型 */
    private AboutPageMediaUsage usageType;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
}
