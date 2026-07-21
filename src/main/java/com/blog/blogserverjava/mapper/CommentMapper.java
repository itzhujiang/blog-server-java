package com.blog.blogserverjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.blogserverjava.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
