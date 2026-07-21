package com.blog.blogserverjava.dto.sitesetting;

import lombok.Data;

@Data
public class SiteSettingListRequest {
    private Integer page = 1;
    private Integer size = 10;
}
