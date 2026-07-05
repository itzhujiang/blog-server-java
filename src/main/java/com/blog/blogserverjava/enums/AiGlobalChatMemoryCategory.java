package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AiGlobalChatMemoryCategory {
    USER("user"),
    FEEDBACK("feedback"),
    REFERENCE("reference");

    @EnumValue
    private final String value;

    AiGlobalChatMemoryCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
