package com.blog.blogserverjava.service.blog;

import com.blog.blogserverjava.dto.sitesetting.AddSiteSettingRequest;
import com.blog.blogserverjava.dto.sitesetting.SiteSettingListRequest;
import com.blog.blogserverjava.dto.sitesetting.SiteSettingListResponse;
import com.blog.blogserverjava.dto.sitesetting.UpdateSiteSettingRequest;

public interface SiteSettingService {
    SiteSettingListResponse getSiteSettings(SiteSettingListRequest request);

    void addSiteSettings(AddSiteSettingRequest request);

    void updateSiteSettings(UpdateSiteSettingRequest request);
}
