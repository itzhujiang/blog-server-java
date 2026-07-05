package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AiChatUserStatus {
    ACTIVE("active"),
    BLOCKED("blocked");

    @EnumValue
    private final String value;

    AiChatUserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
