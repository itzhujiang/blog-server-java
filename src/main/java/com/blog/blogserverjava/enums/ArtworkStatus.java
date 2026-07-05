package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ArtworkStatus {
    DRAFT("draft"),
    PUBLISHED("published");

    @EnumValue
    private final String value;

    ArtworkStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
