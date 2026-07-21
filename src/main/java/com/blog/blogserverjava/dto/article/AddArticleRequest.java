package com.blog.blogserverjava.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AddArticleRequest {

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "URL友好标识不能为空")
    private String slug;

    /** 列表缩略图临时凭证 */
    private String thumbnailCode;

    @NotBlank(message = "文章摘要不能为空")
    private String excerpt;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    private List<AttachmentItem> attachmentList;

    private List<Integer> categories;
}
