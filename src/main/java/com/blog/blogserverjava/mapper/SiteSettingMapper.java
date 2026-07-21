package com.blog.blogserverjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.blogserverjava.entity.SiteSetting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SiteSettingMapper extends BaseMapper<SiteSetting> {
}
