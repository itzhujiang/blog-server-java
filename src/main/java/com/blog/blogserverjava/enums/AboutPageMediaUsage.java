package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AboutPageMediaUsage {
    AVATAR("avatar"),
    CONTENT("content");

    @EnumValue
    private final String value;

    AboutPageMediaUsage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
