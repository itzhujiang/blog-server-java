package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("ai_chat_sessions")
public class AiChatSession {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 会话唯一标识 */
    private String sessionId;
    /** 所属用户ID */
    private Integer userId;
    /** 会话标题 */
    private String title;
    /** 最后一条消息摘要 */
    private String lastMessagePreview;
    /** 最后一条消息时间（毫秒时间戳） */
    private Long lastMessageAt;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;

    @TableLogic
    private Long deletedAt;
}
