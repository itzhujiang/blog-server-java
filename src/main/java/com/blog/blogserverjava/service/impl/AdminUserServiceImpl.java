package com.blog.blogserverjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.entity.AdminUser;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.AdminUserMapper;
import com.blog.blogserverjava.service.AdminUserService;
import com.blog.blogserverjava.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;

    private final JwtUtils jwtutil;

    public String login(String username, String password) {
        AdminUser admin = adminUserMapper.selectOne(new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, username));
        if (admin == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR,"用户名或密码错误");
        }

        String hash = DigestUtils.md5DigestAsHex((password + admin.getPasswordSalt()).getBytes(StandardCharsets.UTF_8));

        if (!hash.equals(admin.getPasswordHash())) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR,"用户名或密码错误");
        }

        return jwtutil.issueJwt(Map.of(
           "id", admin.getId(),
           "username", admin.getUsername(),
           "name", admin.getDisplayName() != null ? admin.getDisplayName() : "翎羽"
        ));
    }
}
