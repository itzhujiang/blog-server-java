package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AdminStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    LOCKED("locked");

    @EnumValue
    private final String value;

    AdminStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
