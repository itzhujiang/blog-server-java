package com.blog.blogserverjava.common;

import com.blog.blogserverjava.enums.ResultCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    /** 捕获自定义业务异常 */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        return Result.error(e.getResultCode(), e.getMessage());
    }

    /**
     * 捕获所有 Exception
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        // 返回统一错误格式
        return Result.error(ResultCode.INTERNAL_ERROR);
    }

    /**
     * 捕获参数校验失败
     * 当你用 @Valid 校验参数时，校验失败会抛 MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        return Result.error(ResultCode.INTERNAL_ERROR, msg);
    }
}
