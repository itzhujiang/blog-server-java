package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ArticleMediaUsage {
    THUMBNAIL("thumbnail"),
    ATTACHMENT("attachment");

    @EnumValue
    private final String value;

    ArticleMediaUsage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
