package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.AiChatMessageRole;
import com.blog.blogserverjava.enums.AiChatMessageType;
import lombok.Data;

@Data
@TableName("ai_chat_messages")
public class AiChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 消息对外业务ID */
    private String messageId;
    /** 所属会话ID */
    private Long sessionId;
    /** 消息发送方 */
    private AiChatMessageRole role;
    /** 消息业务类型 */
    private AiChatMessageType messageType;
    /** 消息正文内容 */
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
