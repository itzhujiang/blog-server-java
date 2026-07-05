package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("ai_session_memories")
public class AiSessionMemory {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 用户ID */
    private Integer userId;
    /** 线程ID（会话ID） */
    private String threadId;
    /** 记忆内容 */
    private String content;
    /** 已处理到的最后一条消息ID */
    private String lastMessageId;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
