package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("media_files")
public class MediaFile {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 原始文件名 */
    private String originalName;
    /** 存储文件名（UUID格式） */
    private String storedName;
    /** 服务端存储路径 */
    private String filePath;
    /** 访问URL */
    private String fileUrl;
    /** 文件大小（字节） */
    private Long fileSize;
    /** MIME类型 */
    private String mimeType;
    /** 图片宽度 */
    private Integer width;
    /** 图片高度 */
    private Integer height;
    /** 图片描述，用于无障碍访问 */
    private String altText;
    /** 上传者 */
    private String uploaderName;
    /** 文件MD5 */
    private String fileHash;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
