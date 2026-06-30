package com.blog.blogserverjava.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    INTERNAL_ERROR(500, "服务器错误");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
