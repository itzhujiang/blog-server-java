package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.AdminStatus;
import lombok.Data;

@Data
@TableName("admin_users")
public class AdminUser {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 登录用户名 */
    private String username;
    /** 邮箱地址 */
    private String email;
    /** 显示名称 */
    private String displayName;
    /** 头像URL */
    private String avatarUrl;
    /** 电话号码 */
    private String phone;
    /** 密码哈希值 */
    private String passwordHash;
    /** 密码盐值 */
    private String passwordSalt;
    /** 账户状态 */
    private AdminStatus status;
    /** 最后登录时间（毫秒时间戳） */
    private Long lastLoginAt;
    /** 最后登录IP */
    private String lastLoginIp;
    /** 连续登录失败次数 */
    private Integer loginAttempts;
    /** 账户锁定到期时间（毫秒时间戳） */
    private Long lockedUntil;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
