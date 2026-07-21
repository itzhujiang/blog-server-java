package com.blog.blogserverjava.service.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.dto.sitesetting.AddSiteSettingRequest;
import com.blog.blogserverjava.dto.sitesetting.SiteSettingListRequest;
import com.blog.blogserverjava.dto.sitesetting.SiteSettingListResponse;
import com.blog.blogserverjava.dto.sitesetting.UpdateSiteSettingRequest;
import com.blog.blogserverjava.entity.SiteSetting;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.SiteSettingMapper;
import com.blog.blogserverjava.service.blog.SiteSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteSettingServiceImpl implements SiteSettingService {

    private final SiteSettingMapper siteSettingMapper;

    @Override
    public SiteSettingListResponse getSiteSettings(SiteSettingListRequest request) {
        Page<SiteSetting> page = siteSettingMapper.selectPage(
                new Page<>(request.getPage(), request.getSize()),
                new LambdaQueryWrapper<SiteSetting>().orderByAsc(SiteSetting::getId)
        );
        return new SiteSettingListResponse(
                page.getRecords(),
                new SiteSettingListResponse.Pagination(request.getPage(), request.getSize(), page.getTotal())
        );
    }

    @Override
    public void addSiteSettings(AddSiteSettingRequest request) {
        SiteSetting existing = siteSettingMapper.selectOne(
                new LambdaQueryWrapper<SiteSetting>().eq(SiteSetting::getSettingKey, request.getSettingKey())
        );
        if (existing != null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "已存在该站点设置");
        }
        SiteSetting setting = new SiteSetting();
        setting.setSettingKey(request.getSettingKey());
        setting.setSettingValue(request.getSettingValue());
        setting.setSettingType(request.getSettingType());
        setting.setDescription(request.getDescription());
        siteSettingMapper.insert(setting);
    }

    @Override
    public void updateSiteSettings(UpdateSiteSettingRequest request) {
        SiteSetting setting = siteSettingMapper.selectById(request.getId());
        if (setting == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "不存在该站点设置");
        }
        setting.setSettingKey(request.getSettingKey());
        setting.setSettingValue(request.getSettingValue());
        setting.setSettingType(request.getSettingType());
        setting.setDescription(request.getDescription());
        siteSettingMapper.updateById(setting);
    }
}
