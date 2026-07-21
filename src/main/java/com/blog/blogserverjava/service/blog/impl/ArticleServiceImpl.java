package com.blog.blogserverjava.service.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.dto.article.*;
import com.blog.blogserverjava.dto.tool.ConfirmMediaResult;
import com.blog.blogserverjava.entity.*;
import com.blog.blogserverjava.enums.ArticleMediaUsage;
import com.blog.blogserverjava.enums.ArticleStatus;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.*;
import com.blog.blogserverjava.service.blog.ArticleService;
import com.blog.blogserverjava.service.tool.MediaFileService;
import com.blog.blogserverjava.utils.ArticleContentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleMediaMapper articleMediaMapper;
    private final ArticleCategoryMapper articleCategoryMapper;
    private final CategoryMapper categoryMapper;
    private final MediaFileMapper mediaFileMapper;
    private final MediaFileService mediaFileService;

    @Override
    public ArticleListResponse getArticleList(ArticleListRequest request) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>()
                .like(StringUtils.hasText(request.getTitle()), Article::getTitle, request.getTitle())
                .eq(request.getStatus() != null, Article::getStatus, request.getStatus())
                .ge(request.getPublishedAtStart() != null, Article::getPublishedAt, request.getPublishedAtStart())
                .le(request.getPublishedAtEnd() != null, Article::getPublishedAt, request.getPublishedAtEnd());

        // 按分类过滤：先查出该分类下的文章ID
        if (request.getCategoryId() != null) {
            List<Integer> articleIds = articleCategoryMapper.selectList(
                    new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getCategoryId, request.getCategoryId())
            ).stream().map(ArticleCategory::getArticleId).toList();
            if (articleIds.isEmpty()) {
                return new ArticleListResponse(List.of(),
                        new ArticleListResponse.Pagination(request.getPage(), request.getSize(), 0L));
            }
            wrapper.in(Article::getId, articleIds);
        }

        if ("asc".equalsIgnoreCase(request.getViewCountSort())) {
            wrapper.orderByAsc(Article::getViewCount);
        } else if ("desc".equalsIgnoreCase(request.getViewCountSort())) {
            wrapper.orderByDesc(Article::getViewCount);
        }
        wrapper.orderByDesc(Article::getPublishedAt);

        Page<Article> page = articleMapper.selectPage(new Page<>(request.getPage(), request.getSize()), wrapper);
        List<Article> articles = page.getRecords();
        if (articles.isEmpty()) {
            return new ArticleListResponse(List.of(),
                    new ArticleListResponse.Pagination(request.getPage(), request.getSize(), page.getTotal()));
        }

        List<Integer> ids = articles.stream().map(Article::getId).toList();

        // 批量查询分类关联
        List<ArticleCategory> articleCategories = articleCategoryMapper.selectList(
                new LambdaQueryWrapper<ArticleCategory>().in(ArticleCategory::getArticleId, ids)
        );
        Set<Integer> categoryIds = articleCategories.stream().map(ArticleCategory::getCategoryId).collect(Collectors.toSet());
        Map<Integer, Category> categoryMap = categoryIds.isEmpty() ? Map.of() :
                categoryMapper.selectList(new LambdaQueryWrapper<Category>().in(Category::getId, categoryIds))
                        .stream().collect(Collectors.toMap(Category::getId, c -> c));
        Map<Integer, List<Integer>> articleIdToCategoryIds = articleCategories.stream()
                .collect(Collectors.groupingBy(ArticleCategory::getArticleId,
                        Collectors.mapping(ArticleCategory::getCategoryId, Collectors.toList())));

        // 批量查询媒体关联（缩略图 + 附件）
        List<ArticleMedia> articleMedias = articleMediaMapper.selectList(
                new LambdaQueryWrapper<ArticleMedia>().in(ArticleMedia::getArticleId, ids)
        );
        Set<Integer> mediaIds = articleMedias.stream().map(ArticleMedia::getMediaId).collect(Collectors.toSet());
        Map<Integer, String> mediaUrlMap = mediaIds.isEmpty() ? Map.of() :
                mediaFileMapper.selectList(new LambdaQueryWrapper<MediaFile>().in(MediaFile::getId, mediaIds))
                        .stream().collect(Collectors.toMap(MediaFile::getId, MediaFile::getFileUrl));
        Map<Integer, List<ArticleMedia>> articleIdToMedias = articleMedias.stream()
                .collect(Collectors.groupingBy(ArticleMedia::getArticleId));

        List<ArticleListResponse.ArticleItem> items = articles.stream().map(article -> {
            List<ArticleListResponse.CategoryBrief> categories = articleIdToCategoryIds
                    .getOrDefault(article.getId(), List.of()).stream()
                    .map(categoryMap::get)
                    .filter(Objects::nonNull)
                    .map(c -> new ArticleListResponse.CategoryBrief(c.getId(), c.getName(), c.getSlug()))
                    .toList();

            List<ArticleMedia> medias = articleIdToMedias.getOrDefault(article.getId(), List.of());
            String thumbnailUrl = medias.stream()
                    .filter(m -> m.getUsageType() == ArticleMediaUsage.THUMBNAIL)
                    .map(m -> mediaUrlMap.get(m.getMediaId()))
                    .filter(Objects::nonNull)
                    .findFirst().orElse("");
            List<String> attachmentUrlArr = medias.stream()
                    .filter(m -> m.getUsageType() == ArticleMediaUsage.ATTACHMENT)
                    .map(m -> mediaUrlMap.get(m.getMediaId()))
                    .filter(Objects::nonNull)
                    .toList();

            return new ArticleListResponse.ArticleItem(
                    article.getId(), article.getTitle(), article.getSlug(), article.getExcerpt(),
                    article.getContent() != null ? article.getContent() : "", thumbnailUrl, attachmentUrlArr,
                    article.getAuthorName(), article.getReadingTime(), article.getViewCount(),
                    article.getStatus(), article.getPublishedAt(), categories
            );
        }).toList();

        return new ArticleListResponse(items,
                new ArticleListResponse.Pagination(request.getPage(), request.getSize(), page.getTotal()));
    }

    private List<String> getUniqueCodes(List<String> codes) {
        return codes.stream().filter(StringUtils::hasText).distinct().toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(AddArticleRequest request, String authorName) {
        Article existing = articleMapper.selectOne(
                new LambdaQueryWrapper<Article>().eq(Article::getSlug, request.getSlug())
        );
        if (existing != null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "文章URL友好标识已存在，请更换后重新提交");
        }

        List<Integer> categories = request.getCategories() != null ? request.getCategories() : List.of();
        if (!categories.isEmpty()) {
            Long count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>().in(Category::getId, categories));
            if (count == 0) {
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "所选分类不存在，请更换后重新提交");
            }
        }

        List<String> attachmentCodes = request.getAttachmentList() != null
                ? request.getAttachmentList().stream().map(AttachmentItem::code).toList()
                : List.of();
        List<String> codeArr = new ArrayList<>(getUniqueCodes(
                java.util.stream.Stream.concat(
                        java.util.stream.Stream.of(request.getThumbnailCode()),
                        attachmentCodes.stream()
                ).toList()
        ));

        List<ConfirmMediaResult> confirmResults = codeArr.isEmpty()
                ? List.of() : mediaFileService.confirmTempMedia(codeArr);

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSlug(request.getSlug());
        article.setContent("");
        article.setExcerpt(request.getExcerpt());
        article.setAuthorName(authorName != null ? authorName : "翎羽");
        article.setStatus(ArticleStatus.PUBLISHED);
        article.setReadingTime(0);
        articleMapper.insert(article);

        for (ConfirmMediaResult result : confirmResults) {
            ArticleMedia media = new ArticleMedia();
            media.setArticleId(article.getId());
            media.setMediaId(result.mediaId());
            media.setUsageType(result.fileCode().equals(request.getThumbnailCode())
                    ? ArticleMediaUsage.THUMBNAIL : ArticleMediaUsage.ATTACHMENT);
            media.setSortOrder(0);
            articleMediaMapper.insert(media);
        }

        for (Integer categoryId : categories) {
            ArticleCategory ac = new ArticleCategory();
            ac.setArticleId(article.getId());
            ac.setCategoryId(categoryId);
            articleCategoryMapper.insert(ac);
        }

        // 处理文章内容：替换临时路径为永久路径
        Map<String, String> mapping = buildMapping(request.getAttachmentList(), confirmResults);
        String processedContent = ArticleContentProcessor.process(request.getContent(), mapping);
        article.setContent(processedContent);
        article.setReadingTime((int) Math.ceil(processedContent.length() / 500.0));
        articleMapper.updateById(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(UpdateArticleRequest request, String authorName) {
        Article existing = articleMapper.selectOne(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getSlug, request.getSlug())
                        .ne(Article::getId, request.getId())
        );
        if (existing != null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "文章URL友好标识已存在，请更换后重新提交");
        }

        List<Integer> categories = request.getCategories() != null ? request.getCategories() : List.of();
        if (!categories.isEmpty()) {
            Long count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>().in(Category::getId, categories));
            if (count == 0) {
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "所选分类不存在，请更换后重新提交");
            }
        }

        Article article = articleMapper.selectById(request.getId());
        if (article == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "文章不存在，无法更新");
        }

        // 重建分类关联
        articleCategoryMapper.delete(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getArticleId, request.getId()));
        for (Integer categoryId : categories) {
            ArticleCategory ac = new ArticleCategory();
            ac.setArticleId(request.getId());
            ac.setCategoryId(categoryId);
            articleCategoryMapper.insert(ac);
        }

        List<String> attachmentCodes = (request.isUpdateArticle() && request.getAttachmentList() != null)
                ? request.getAttachmentList().stream().map(AttachmentItem::code).toList()
                : List.of();
        List<String> codeArr = new ArrayList<>(getUniqueCodes(
                java.util.stream.Stream.concat(
                        java.util.stream.Stream.of(request.isUpdateThumbnail() ? request.getThumbnailCode() : null),
                        attachmentCodes.stream()
                ).toList()
        ));

        List<ConfirmMediaResult> confirmResults = codeArr.isEmpty()
                ? List.of() : mediaFileService.confirmTempMedia(codeArr);

        article.setTitle(request.getTitle());
        article.setSlug(request.getSlug());
        article.setExcerpt(request.getExcerpt());
        article.setAuthorName(authorName != null ? authorName : "翎羽");

        if (request.isUpdateThumbnail()) {
            articleMediaMapper.delete(new LambdaQueryWrapper<ArticleMedia>()
                    .eq(ArticleMedia::getArticleId, request.getId())
                    .eq(ArticleMedia::getUsageType, ArticleMediaUsage.THUMBNAIL));

            if (StringUtils.hasText(request.getThumbnailCode())) {
                ConfirmMediaResult thumbnailFile = confirmResults.stream()
                        .filter(r -> r.fileCode().equals(request.getThumbnailCode()))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(ResultCode.INTERNAL_ERROR, "缩略图确认失败"));
                ArticleMedia media = new ArticleMedia();
                media.setArticleId(request.getId());
                media.setMediaId(thumbnailFile.mediaId());
                media.setUsageType(ArticleMediaUsage.THUMBNAIL);
                media.setSortOrder(0);
                articleMediaMapper.insert(media);
            }
        }

        if (request.isUpdateArticle() && request.getAttachmentList() != null) {
            articleMediaMapper.delete(new LambdaQueryWrapper<ArticleMedia>()
                    .eq(ArticleMedia::getArticleId, request.getId())
                    .eq(ArticleMedia::getUsageType, ArticleMediaUsage.ATTACHMENT));

            for (AttachmentItem attachment : request.getAttachmentList()) {
                confirmResults.stream()
                        .filter(r -> r.fileCode().equals(attachment.code()))
                        .findFirst()
                        .ifPresent(attachmentFile -> {
                            ArticleMedia media = new ArticleMedia();
                            media.setArticleId(request.getId());
                            media.setMediaId(attachmentFile.mediaId());
                            media.setUsageType(ArticleMediaUsage.ATTACHMENT);
                            media.setSortOrder(0);
                            articleMediaMapper.insert(media);
                        });
            }
        }

        if (request.isUpdateArticle() && request.getContent() != null) {
            Map<String, String> mapping = buildMapping(request.getAttachmentList(), confirmResults);
            String processedContent = ArticleContentProcessor.process(request.getContent(), mapping);
            article.setContent(processedContent);
            article.setReadingTime((int) Math.ceil(processedContent.length() / 500.0));
        }

        articleMapper.updateById(article);
    }

    @Override
    public void delArticle(Integer id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "文章不存在，无法删除");
        }
        articleMapper.deleteById(id);
    }

    private Map<String, String> buildMapping(List<AttachmentItem> attachmentList, List<ConfirmMediaResult> confirmResults) {
        Map<String, String> mapping = new HashMap<>();
        if (attachmentList == null) {
            return mapping;
        }
        for (AttachmentItem item : attachmentList) {
            confirmResults.stream()
                    .filter(r -> r.fileCode().equals(item.code()))
                    .findFirst()
                    .ifPresent(result -> mapping.put(item.source(), result.fileUrl()));
        }
        return mapping;
    }
}
