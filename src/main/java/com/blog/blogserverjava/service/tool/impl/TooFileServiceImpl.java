package com.blog.blogserverjava.service.tool.impl;

import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.dto.tool.UploadResponse;
import com.blog.blogserverjava.entity.TempMedia;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.TempMediaMapper;
import com.blog.blogserverjava.service.tool.ToolFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TooFileServiceImpl implements ToolFileService  {

    @Value("${app.uploads-dir}")
    private String uploadsDir;

    @Value("${app.uploads-url}")
    private String uploadsUrl;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    private final TempMediaMapper tempMediaMapper;

    private long tempFileExpiry = 24 * 60 * 60 * 1000;

    @Override
    public UploadResponse upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "文件不能为空");
        }
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID() + ext;
        // 存储路径
        Path path = Paths.get(uploadsDir, "temp");
        // 确保目录存在
        Files.createDirectories(path);

        Path dest = path.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        // 生成 code
        String code =  UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        long expiresAt = timestamp + tempFileExpiry;
        String dir = uploadsUrl + "/temp/" +  filename;
        String url = contextPath + dir;
        TempMedia tempMedia = new TempMedia();
        tempMedia.setCode(code);
        tempMedia.setOriginalName(file.getOriginalFilename());
        tempMedia.setStoredName(filename);
        tempMedia.setFilePath(dir);
        tempMedia.setFileSize(file.getSize());
        tempMedia.setMimeType(file.getContentType());
        tempMedia.setExpiresAt(expiresAt);
        tempMedia.setIsUsed(false);

        tempMediaMapper.insert(tempMedia);

        return new UploadResponse(new UploadResponse.Data(code, file.getSize(), url, file.getOriginalFilename()));
    }
}
