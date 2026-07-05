package com.blog.blogserverjava.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.blogserverjava.enums.SettingType;
import lombok.Data;

@Data
@TableName("site_settings")
public class SiteSetting {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 配置键名 */
    private String settingKey;
    /** 配置值 */
    private String settingValue;
    /** 值类型 */
    private SettingType settingType;
    /** 配置说明 */
    private String description;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}
