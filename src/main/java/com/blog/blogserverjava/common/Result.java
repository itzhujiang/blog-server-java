package com.blog.blogserverjava.common;

public class Result<T> {
    private int code; // 200 成功，500 失败，401 未登录
    private String msg;
    private  T data;

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> sussess(T data) {
        return new Result<>(200, "成功", data);
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result<>(200, msg, data);
    }

    public static <T> Result<T> error(String msg) {
        return  new Result<>(500, msg, null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

}
