package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.CommentStatus;
import lombok.Data;

@Data
@TableName("comments")
public class Comment {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 关联文章ID */
    private Integer articleId;
    /** 父评论ID，null表示顶级评论 */
    private Integer parentId;
    /** 访客姓名 */
    private String authorName;
    /** 是否为作者 */
    private Boolean isAuthor;
    /** 访客邮箱 */
    private String authorEmail;
    /** 访客手机号 */
    private String authorPhone;
    /** IP地址 */
    private String authorIp;
    /** 评论内容 */
    private String content;
    /** 审核状态 */
    private CommentStatus status;
    /** 点赞数 */
    private Integer likeCount;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
