package com.blog.blogserverjava.interceptor;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.utils.JwtPayload;
import com.blog.blogserverjava.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;

    private void handlerNonToken(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        Result<?> result = Result.error(ResultCode.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        System.out.println("-1-1-1-1");
        String authorization = request.getHeader("authorization");
        if (!StringUtils.hasText(authorization)) {
            this.handlerNonToken(response);
            return  false;
        }

        String[] tokenArr = authorization.split(" ");
        String token = tokenArr.length == 1 ? tokenArr[0] : tokenArr[1];
        JwtPayload jwtPayload = jwtUtils.verifyJwt(token);
        if (jwtPayload == null) {
            this.handlerNonToken(response);
            return false;
        }
        request.setAttribute("user", jwtPayload);
        return true;
    }
}
