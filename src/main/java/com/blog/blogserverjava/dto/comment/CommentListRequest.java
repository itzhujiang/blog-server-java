package com.blog.blogserverjava.dto.comment;

import com.blog.blogserverjava.enums.CommentStatus;
import lombok.Data;

@Data
public class CommentListRequest {
    private Integer page = 1;
    private Integer size = 10;
    private Integer id;
    private Integer parentId;
    private CommentStatus status;
    private String authorName;
    private Integer articleId;
    /** asc / desc */
    private String likeCountSort;
    private Long createDateTimeStart;
    private Long createDateTimeEnd;
}
