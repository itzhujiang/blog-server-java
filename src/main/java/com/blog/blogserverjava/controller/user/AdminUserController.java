package com.blog.blogserverjava.controller.user;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.dto.user.LoginRequest;
import com.blog.blogserverjava.dto.user.LoginResponse;
import com.blog.blogserverjava.dto.user.UserInfoResponse;
import com.blog.blogserverjava.service.user.AdminUserService;
import com.blog.blogserverjava.utils.JwtPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = adminUserService.login(req.getUsername(), req.getPassword());
        return Result.success(new LoginResponse(new LoginResponse.Data(token)));
    }

    @GetMapping("/getUesrInfo")
    public Result<UserInfoResponse> getUesrInfo(HttpServletRequest httpRequest) {
        JwtPayload user = (JwtPayload) httpRequest.getAttribute("user");
        return Result.success(adminUserService.getUserInfo(user != null ? user.id() : null));
    }
}
