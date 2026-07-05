package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum BigFileChunkStatus {
    PENDING("pending"),
    UPLOADED("uploaded");

    @EnumValue
    private final String value;

    BigFileChunkStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
