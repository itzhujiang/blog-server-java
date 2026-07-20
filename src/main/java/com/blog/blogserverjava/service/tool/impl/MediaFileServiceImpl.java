package com.blog.blogserverjava.service.tool.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.dto.tool.ConfirmMediaResult;
import com.blog.blogserverjava.entity.MediaFile;
import com.blog.blogserverjava.entity.TempMedia;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.MediaFileMapper;
import com.blog.blogserverjava.mapper.TempMediaMapper;
import com.blog.blogserverjava.service.tool.MediaFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl implements MediaFileService {

    @Value("${app.uploads-dir}")
    private String uploadsDir;

    @Value("${app.uploads-url}")
    private String uploadsUrl;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    private final TempMediaMapper tempMediaMapper;
    private final MediaFileMapper mediaFileMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public List<ConfirmMediaResult> confirmTempMedia(List<String> codes) {
        List<String> validCodes = codes.stream().filter(StringUtils::hasText).toList();
        if (validCodes.size() != codes.size()) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "存在无效的文件凭证");
        }
        Map<String ,TempMedia> tempMedias = tempMediaMapper.selectList(
            new LambdaQueryWrapper<TempMedia>().in(TempMedia::getCode, validCodes)
        ).stream().collect(Collectors.toMap(TempMedia::getCode, t -> t));
        Set<String> foundCodes = tempMedias.keySet();
        String missingCode = validCodes.stream().filter(c -> !foundCodes.contains(c)).findFirst().orElse(null);
        if (missingCode != null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "文件未上传: " + missingCode);
        }
        List<ConfirmMediaResult> results = new ArrayList<>();
        long timestamp = System.currentTimeMillis();
        for (String item : validCodes) {

            TempMedia tempMedia = tempMedias.get(item);
            String filePath = tempMedia.getFilePath().startsWith("/") ? tempMedia.getFilePath().substring(1) : tempMedia.getFilePath();
            Path oldFilePath = Paths.get(System.getProperty("user.dir"), filePath);
            // 临时文件以使用
            if (tempMedia.getIsUsed()) {
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "临时文件被使用");
            }
            // 超时
            if (timestamp > tempMedia.getExpiresAt()) {
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException ignored) {}
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "临时文件已过期");
            }

            // 读取文件内容并计算 MD5 hash
            byte[] fileBytes = Files.readAllBytes(oldFilePath);
            String fileHash = DigestUtils.md5DigestAsHex(fileBytes);

            MediaFile existingFile = mediaFileMapper.selectOne(
               new LambdaQueryWrapper<MediaFile>().eq(MediaFile::getFileHash, fileHash)
            );
            // 文件已存在
            if (existingFile != null) {
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException ignored) {}
                tempMedia.setIsUsed(true);
                tempMediaMapper.updateById(tempMedia);
                results.add(new ConfirmMediaResult(item, existingFile.getId(), existingFile.getFileUrl()));
                continue;
            }
            // 文件不存在，创建新记录
            String year = String.valueOf(LocalDate.now().getYear());
            String month = String.format("%02d", LocalDate.now().getMonthValue());
            String path = uploadsUrl + "/file/" + year + "/" + month + "/" + tempMedia.getStoredName();

            MediaFile mediaFile = new MediaFile();
            mediaFile.setOriginalName(tempMedia.getOriginalName());
            mediaFile.setStoredName(tempMedia.getStoredName());
            mediaFile.setFilePath(path);
            mediaFile.setFileUrl(contextPath + path);
            mediaFile.setFileSize(tempMedia.getFileSize());
            mediaFile.setMimeType(tempMedia.getMimeType());
            mediaFile.setFileHash(fileHash);
            mediaFileMapper.insert(mediaFile);
            tempMedia.setIsUsed(true);
            tempMediaMapper.updateById(tempMedia);

            // 事务提交后移动文件
            Path newFilePath = Paths.get(uploadsDir, "file", year, month, tempMedia.getStoredName());

            eventPublisher.publishEvent(new com.blog.blogserverjava.event.FileMoveEvent(oldFilePath, newFilePath));

            results.add(new ConfirmMediaResult(item, mediaFile.getId(), contextPath + path));
        }
        return results;
    }
}
