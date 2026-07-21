package com.blog.blogserverjava.dto.user;

public record UserInfoResponse(Data data) {

    public record Data(Integer id, String email, String avatarUrl, String displayName, String username) {
    }
}
