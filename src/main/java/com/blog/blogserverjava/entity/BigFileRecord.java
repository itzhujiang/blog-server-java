package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.BigFileStatus;
import lombok.Data;

@Data
@TableName("big_file_records")
public class BigFileRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 文件唯一标识（MD5） */
    private String identifier;
    /** 原始文件名 */
    private String originalName;
    /** 文件总大小（字节） */
    private Long totalSize;
    /** 分片大小（字节） */
    private Integer chunkSize;
    /** 总分片数 */
    private Integer totalChunks;
    /** MIME类型 */
    private String mimeType;
    /** 上传状态 */
    private BigFileStatus status;
    /** 文件MD5，用于秒传 */
    private String fileHash;
    /** 合并后存储文件名 */
    private String storedName;
    /** 合并后文件路径 */
    private String filePath;
    /** 合并后文件URL */
    private String fileUrl;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    /** 完成时间（毫秒时间戳） */
    private Long completedAt;
}
