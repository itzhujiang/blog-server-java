package com.blog.blogserverjava.dto.aboutme;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAboutMeRequest {

    @NotNull(message = "关于我信息ID不能为空")
    private Integer id;

    private String jobTitle;
    private String personalTags;
    private String contactInfo;
    private String socialLinks;
    private String skills;
    private String timeline;

    /** 头像临时文件凭证 */
    private String avatarCode;
    /** 内容临时文件凭证 */
    private String contentCode;
    /** 是否更新头像 */
    private boolean updateAvatar;
    /** 是否更新内容 */
    private boolean updateContent;
}
