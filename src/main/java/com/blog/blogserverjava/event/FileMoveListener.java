package com.blog.blogserverjava.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
public class FileMoveListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onFileMove(FileMoveEvent event) {
        try {
            Files.createDirectories(event.dest().getParent());
            Files.move(event.src(), event.dest(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("文件移动失败: src={}, dest={}", event.src(), event.dest(), e);
        }
    }
}
