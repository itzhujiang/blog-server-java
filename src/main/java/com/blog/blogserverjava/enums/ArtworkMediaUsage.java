package com.blog.blogserverjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ArtworkMediaUsage {
    MAIN("main"),
    THUMBNAIL("thumbnail"),
    PROCESS("process"),
    VARIANT("variant");

    @EnumValue
    private final String value;

    ArtworkMediaUsage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
