package com.blog.blogserverjava.common;

import com.blog.blogserverjava.enums.ResultCode;
import lombok.Getter;

@Getter
public class Result<T> {
    private int code; // 200 成功，500 失败，401 未登录
    private String msg;
    private  T data;

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> Result<T> error(ResultCode resultCode, String msg) {
        return new Result<>(resultCode.getCode(), msg, null);
    }
}
