package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.AiGlobalChatMemoryCategory;
import lombok.Data;

@Data
@TableName("ai_global_chat_memories")
public class AiGlobalChatMemory {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 用户ID */
    private Integer userId;
    /** 记忆主题 */
    private AiGlobalChatMemoryCategory category;
    /** 内容 */
    private String content;
    /** 访问次数 */
    private Integer accessCount;
    /** 最后访问时间（毫秒时间戳） */
    private Long lastAccessedAt;
    /** 是否为索引信息 */
    private Boolean skipIndex;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;

    @TableLogic
    private Long deletedAt;
}
