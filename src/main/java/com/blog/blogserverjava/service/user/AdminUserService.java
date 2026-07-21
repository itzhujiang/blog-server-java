package com.blog.blogserverjava.service.user;

import com.blog.blogserverjava.dto.user.UserInfoResponse;

public interface AdminUserService {
    String login(String username, String password);

    UserInfoResponse getUserInfo(Integer id);
}
