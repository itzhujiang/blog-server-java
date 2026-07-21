package com.blog.blogserverjava.dto.sitesetting;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateSiteSettingRequest extends AddSiteSettingRequest {

    @NotNull(message = "站点设置ID不能为空")
    private Integer id;
}
