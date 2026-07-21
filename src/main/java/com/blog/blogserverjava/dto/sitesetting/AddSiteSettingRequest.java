package com.blog.blogserverjava.dto.sitesetting;

import com.blog.blogserverjava.enums.SettingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddSiteSettingRequest {

    @NotBlank(message = "配置键名不能为空")
    private String settingKey;

    @NotBlank(message = "配置值不能为空")
    private String settingValue;

    @NotNull(message = "值类型不能为空")
    private SettingType settingType;

    private String description;
}
