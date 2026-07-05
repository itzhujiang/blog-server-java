package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AiChatMessageType {
    TEXT("text"),
    A2UI("A2UI");

    @EnumValue
    private final String value;

    AiChatMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
