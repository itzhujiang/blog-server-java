package com.blog.blogserverjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.blogserverjava.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
};
