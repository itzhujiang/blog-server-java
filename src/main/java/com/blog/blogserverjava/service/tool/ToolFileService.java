package com.blog.blogserverjava.service.tool;

import com.blog.blogserverjava.dto.tool.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ToolFileService {
    UploadResponse upload(MultipartFile file) throws IOException;
}
