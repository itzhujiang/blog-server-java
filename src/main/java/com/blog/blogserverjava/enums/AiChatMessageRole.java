package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AiChatMessageRole {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    @EnumValue
    private final String value;

    AiChatMessageRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
