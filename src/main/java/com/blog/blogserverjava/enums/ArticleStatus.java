package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ArticleStatus {
    DRAFT("draft"),
    PUBLISHED("published"),
    ARCHIVED("archived");

    @EnumValue
    private final String value;

    ArticleStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
