package com.blog.blogserverjava.controller.tool;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.dto.tool.UploadResponse;
import com.blog.blogserverjava.service.tool.ToolFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/tool/file")
@RequiredArgsConstructor
public class ToolFileController {
    private final ToolFileService toolFileService;

    @PostMapping("/upload")
    public Result<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        UploadResponse uploadResponse = toolFileService.upload(file);
        return Result.success(uploadResponse);
    };
}
