package com.blog.blogserverjava.controller;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.dto.admin.LoginRequest;
import com.blog.blogserverjava.dto.admin.LoginResponse;
import com.blog.blogserverjava.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @RequestMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = adminUserService.login(req.getUsername(), req.getPassword());
        return Result.success(new LoginResponse(token));
    }
}
