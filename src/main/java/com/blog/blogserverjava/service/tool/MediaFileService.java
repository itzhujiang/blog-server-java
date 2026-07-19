package com.blog.blogserverjava.service.tool;

import com.blog.blogserverjava.dto.tool.ConfirmMediaResult;

import java.util.List;

public interface MediaFileService {
    List<ConfirmMediaResult> confirmTempMedia(List<String> codes);
}
