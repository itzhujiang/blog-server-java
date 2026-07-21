package com.blog.blogserverjava.controller.blog;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.dto.sitesetting.AddSiteSettingRequest;
import com.blog.blogserverjava.dto.sitesetting.SiteSettingListRequest;
import com.blog.blogserverjava.dto.sitesetting.SiteSettingListResponse;
import com.blog.blogserverjava.dto.sitesetting.UpdateSiteSettingRequest;
import com.blog.blogserverjava.service.blog.SiteSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/site-setting")
@RequiredArgsConstructor
public class SiteSettingController {

    private final SiteSettingService siteSettingService;

    @GetMapping("/getSettings")
    public Result<SiteSettingListResponse> getSettings(SiteSettingListRequest request) {
        return Result.success(siteSettingService.getSiteSettings(request));
    }

    @PostMapping("/addSettings")
    public Result<Void> addSettings(@Valid @RequestBody AddSiteSettingRequest request) {
        siteSettingService.addSiteSettings(request);
        return Result.success(null);
    }

    @PutMapping("/updateSettings")
    public Result<Void> updateSettings(@Valid @RequestBody UpdateSiteSettingRequest request) {
        siteSettingService.updateSiteSettings(request);
        return Result.success(null);
    }
}
