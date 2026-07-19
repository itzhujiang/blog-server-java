package com.blog.blogserverjava.event;

import java.nio.file.Path;

public record FileMoveEvent(Path src, Path dest) {
}
