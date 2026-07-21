package com.blog.blogserverjava.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCommentRequest {

    @NotNull(message = "评论ID不能为空")
    private Integer id;

    /** approved 或 spam */
    @NotNull(message = "审核状态不能为空")
    private String status;
}
