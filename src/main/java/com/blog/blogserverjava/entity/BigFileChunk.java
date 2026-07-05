package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.BigFileChunkStatus;
import lombok.Data;

@Data
@TableName("big_file_chunks")
public class BigFileChunk {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 关联大文件标识 */
    private String fileIdentifier;
    /** 分片序号（从1开始） */
    private Integer chunkNumber;
    /** 分片大小（字节） */
    private Integer chunkSize;
    /** 分片MD5 */
    private String chunkHash;
    /** 分片存储路径 */
    private String chunkPath;
    /** 上传状态 */
    private BigFileChunkStatus status;
    /** 上传时间（毫秒时间戳） */
    private Long uploadedAt;
}
