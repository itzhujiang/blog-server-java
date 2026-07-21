package com.blog.blogserverjava.dto.aboutme;

public record AboutMeInfoResponse(
        Integer id,
        String title,
        String nickname,
        String jobTitle,
        String personalTags,
        String contactInfo,
        String socialLinks,
        String skills,
        String timeline,
        Long updatedAt,
        String avatarUrl,
        String contentUrl
) {
}
