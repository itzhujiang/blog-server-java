package com.blog.blogserverjava.dto.aboutme;

import java.util.List;

/**
 * 复刻 Node.js 遗留格式：单个对象包成长度为1的数组 + 固定 pagination。
 */
public record AboutMeInfoWrapper(List<AboutMeInfoResponse> data, Pagination pagination) {

    public record Pagination(Integer page, Integer size, Long total) {
    }
}
