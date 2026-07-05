package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.AiChatUserStatus;
import lombok.Data;

@Data
@TableName("ai_chat_users")
public class AiChatUser {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 手机号 */
    private String phone;
    /** 用户状态 */
    private AiChatUserStatus status;
    /** 上次验证时间（毫秒时间戳） */
    private Long lastVerifiedAt;
    /** 上次登录IP */
    private String lastLoginIp;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
