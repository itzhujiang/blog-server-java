package com.blog.blogserverjava.controller.blog;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.dto.article.AddArticleRequest;
import com.blog.blogserverjava.dto.article.ArticleListRequest;
import com.blog.blogserverjava.dto.article.ArticleListResponse;
import com.blog.blogserverjava.dto.article.UpdateArticleRequest;
import com.blog.blogserverjava.service.blog.ArticleService;
import com.blog.blogserverjava.utils.JwtPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/getArticleList")
    public Result<ArticleListResponse> getArticleList(ArticleListRequest request) {
        return Result.success(articleService.getArticleList(request));
    }

    @PostMapping("/addArticle")
    public Result<Void> addArticle(@Valid @RequestBody AddArticleRequest request, HttpServletRequest httpRequest) {
        JwtPayload user = (JwtPayload) httpRequest.getAttribute("user");
        articleService.addArticle(request, user != null ? user.name() : null);
        return Result.success(null);
    }

    @PutMapping("/updateArticle")
    public Result<Void> updateArticle(@Valid @RequestBody UpdateArticleRequest request, HttpServletRequest httpRequest) {
        JwtPayload user = (JwtPayload) httpRequest.getAttribute("user");
        articleService.updateArticle(request, user != null ? user.name() : null);
        return Result.success(null);
    }

    @DeleteMapping("/delArticle")
    public Result<Void> delArticle(@RequestParam Integer id) {
        articleService.delArticle(id);
        return Result.success(null);
    }
}
