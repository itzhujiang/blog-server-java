package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum CommentStatus {
    PENDING("pending"),
    APPROVED("approved"),
    SPAM("spam"),
    TRASH("trash");

    @EnumValue
    private final String value;

    CommentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
