package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SettingType {
    STRING("string"),
    NUMBER("number"),
    BOOLEAN("boolean"),
    JSON("json");

    @EnumValue
    private final String value;

    SettingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
