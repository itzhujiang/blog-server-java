package com.blog.blogserverjava.common;

import com.blog.blogserverjava.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 捕获自定义业务异常 */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("""
                \nmessage: {}
                url: {}
                method: {}
                stack: """, e.getMessage(), request.getRequestURI(), request.getMethod(),e);
        return Result.error(e.getResultCode(), e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        return Result.error(ResultCode.INTERNAL_ERROR, "文件大小超出限制，最大允许 2MB");
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        log.error("""
                \nmessage: {}
                url: {}
                method: {}
                stack: """, e.getMessage(), request.getRequestURI(), request.getMethod(), e);
        return Result.error(ResultCode.INTERNAL_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMediaTypeNotSupportedException.class})
    public Result<?> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        log.warn("""
                \nmessage: {}
                url: {}
                method: {}
                stack: """, msg, request.getRequestURI(), request.getMethod(),e);
        return Result.error(ResultCode.INTERNAL_ERROR, msg);
    }
}
