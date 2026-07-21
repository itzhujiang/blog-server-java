package com.blog.blogserverjava.dto.user;

public record LoginResponse(Data data) {

    public record Data(String token) {
    }
}
