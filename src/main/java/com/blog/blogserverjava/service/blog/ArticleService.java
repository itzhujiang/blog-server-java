package com.blog.blogserverjava.service.blog;

import com.blog.blogserverjava.dto.article.AddArticleRequest;
import com.blog.blogserverjava.dto.article.ArticleListRequest;
import com.blog.blogserverjava.dto.article.ArticleListResponse;
import com.blog.blogserverjava.dto.article.UpdateArticleRequest;

public interface ArticleService {
    ArticleListResponse getArticleList(ArticleListRequest request);

    void addArticle(AddArticleRequest request, String authorName);

    void updateArticle(UpdateArticleRequest request, String authorName);

    void delArticle(Integer id);
}
