package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum BigFileStatus {
    UPLOADING("uploading"),
    COMPLETED("completed"),
    FAILED("failed");

    @EnumValue
    private final String value;

    BigFileStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
