package com.blog.blogserverjava.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.blogserverjava.entity.TempMedia;
import com.blog.blogserverjava.mapper.TempMediaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TempMediaCleanupTask {

    private final TempMediaMapper tempMediaMapper;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpiredTempFiles() {
        long timestamp = System.currentTimeMillis();
        List<TempMedia> tempMedias = tempMediaMapper.selectList(
                new LambdaQueryWrapper<TempMedia>().lt(TempMedia::getExpiresAt, timestamp)
        );
        for (TempMedia item : tempMedias) {
            String filePath = item.getFilePath().startsWith("/") ? item.getFilePath().substring(1) : item.getFilePath();
            Path oldFilePath = Paths.get(System.getProperty("user.dir"), filePath);
            try {
                Files.deleteIfExists(oldFilePath);
            } catch (IOException ignored) {}
            tempMediaMapper.deleteById(item.getCode());
        }
        log.info("清理完成，删除了 {} 个过期临时文件", tempMedias.size());
    }
}
