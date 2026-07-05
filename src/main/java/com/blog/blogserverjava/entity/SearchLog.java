package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("search_logs")
public class SearchLog {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 搜索关键词 */
    private String query;
    /** 搜索结果数量 */
    private Integer resultsCount;
    /** IP地址 */
    private String ipAddress;
    /** 用户代理 */
    private String userAgent;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
}
