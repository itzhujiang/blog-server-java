package com.blog.blogserverjava.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReplyCommentRequest {

    @NotNull(message = "评论ID不能为空")
    private Integer id;

    @NotBlank(message = "回复内容不能为空")
    private String content;
}
