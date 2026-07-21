package com.blog.blogserverjava.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.dto.user.UserInfoResponse;
import com.blog.blogserverjava.entity.AdminUser;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.AdminUserMapper;
import com.blog.blogserverjava.service.user.AdminUserService;
import com.blog.blogserverjava.utils.JwtPayload;
import com.blog.blogserverjava.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;

    private final JwtUtils jwtutil;

    public String login(String username, String password) {
        AdminUser admin = adminUserMapper.selectOne(new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, username));
        if (admin == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "用户名或密码错误");
        }

        String hash = DigestUtils.md5DigestAsHex((password + admin.getPasswordSalt()).getBytes(StandardCharsets.UTF_8));

        if (!hash.equals(admin.getPasswordHash())) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "用户名或密码错误");
        }
        JwtPayload jwtPayload = new JwtPayload(admin.getId(), admin.getUsername(), admin.getDisplayName() != null ? admin.getDisplayName() : "翎羽");
        return jwtutil.issueJwt(jwtPayload);
    }

    @Override
    public UserInfoResponse getUserInfo(Integer id) {
        if (id == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "当前用户不存在");
        }
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "当前用户不存在");
        }
        return new UserInfoResponse(new UserInfoResponse.Data(
                user.getId(), user.getEmail(), user.getAvatarUrl(), user.getDisplayName(), user.getUsername()
        ));
    }
}
