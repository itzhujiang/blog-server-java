package com.blog.blogserverjava.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateArticleRequest {

    @NotNull(message = "文章ID不能为空")
    private Integer id;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "URL友好标识不能为空")
    private String slug;

    private String thumbnailCode;

    @NotBlank(message = "文章摘要不能为空")
    private String excerpt;

    /** 文章内容，isUpdateArticle=true 时才生效 */
    private String content;

    private List<AttachmentItem> attachmentList;

    private List<Integer> categories;

    private boolean isUpdateArticle;

    private boolean isUpdateThumbnail;
}
