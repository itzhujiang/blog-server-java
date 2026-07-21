package com.blog.blogserverjava.dto.sitesetting;

import com.blog.blogserverjava.entity.SiteSetting;

import java.util.List;

public record SiteSettingListResponse(List<SiteSetting> data, Pagination pagination) {

    public record Pagination(Integer page, Integer size, Long total) {
    }
}
