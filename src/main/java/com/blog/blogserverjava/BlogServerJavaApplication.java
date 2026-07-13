package com.blog.blogserverjava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.blog.blogserverjava.mapper")
public class BlogServerJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogServerJavaApplication.class, args);
    }

}
