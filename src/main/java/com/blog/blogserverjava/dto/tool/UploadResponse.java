package com.blog.blogserverjava.dto.tool;

public record UploadResponse(Data data) {

    public record Data(String code, Long size, String url, String fileName) {
    }
}
