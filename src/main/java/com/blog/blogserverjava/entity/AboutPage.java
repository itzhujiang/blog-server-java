package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("about_page")
public class AboutPage {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 页面标题 */
    private String title;
    /** 博主昵称 */
    private String nickname;
    /** 职业标签 */
    private String jobTitle;
    /** 个人标签数组（JSON） */
    @TableField(typeHandler = com.blog.blogserverjava.config.PostgresJsonbTypeHandler.class)
    private String personalTags;
    /** 联系方式（JSON） */
    @TableField(typeHandler = com.blog.blogserverjava.config.PostgresJsonbTypeHandler.class)
    private String contactInfo;
    /** 社交媒体链接（JSON） */
    @TableField(typeHandler = com.blog.blogserverjava.config.PostgresJsonbTypeHandler.class)
    private String socialLinks;
    /** 技能专长（JSON） */
    @TableField(typeHandler = com.blog.blogserverjava.config.PostgresJsonbTypeHandler.class)
    private String skills;
    /** 成长足迹（JSON） */
    @TableField(typeHandler = com.blog.blogserverjava.config.PostgresJsonbTypeHandler.class)
    private String timeline;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
