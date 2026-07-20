package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("temp_media")
public class TempMedia {

    /** 临时凭证（UUID），作为主键 */
    @TableId(type = IdType.INPUT)
    private String code;
    /** 原始文件名 */
    private String originalName;
    /** 存储文件名 */
    private String storedName;
    /** 文件路径 */
    private String filePath;
    /** 文件大小（字节） */
    private Long fileSize;
    /** MIME类型 */
    private String mimeType;
    /** 过期时间（毫秒时间戳） */
    private Long expiresAt;
    /** 是否已被使用 */
    private Boolean isUsed;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
    @TableLogic
    private Integer deleted;
}
