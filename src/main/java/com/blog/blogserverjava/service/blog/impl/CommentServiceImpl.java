package com.blog.blogserverjava.service.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.blogserverjava.common.BusinessException;
import com.blog.blogserverjava.dto.comment.*;
import com.blog.blogserverjava.entity.AdminUser;
import com.blog.blogserverjava.entity.Article;
import com.blog.blogserverjava.entity.Comment;
import com.blog.blogserverjava.enums.CommentStatus;
import com.blog.blogserverjava.enums.ResultCode;
import com.blog.blogserverjava.mapper.AdminUserMapper;
import com.blog.blogserverjava.mapper.ArticleMapper;
import com.blog.blogserverjava.mapper.CommentMapper;
import com.blog.blogserverjava.service.blog.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final ArticleMapper articleMapper;
    private final AdminUserMapper adminUserMapper;

    @Override
    public CommentListResponse getCommentsList(CommentListRequest request) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<Comment>()
                .eq(request.getId() != null, Comment::getId, request.getId())
                .eq(request.getParentId() != null, Comment::getParentId, request.getParentId())
                .eq(request.getStatus() != null, Comment::getStatus, request.getStatus())
                .like(StringUtils.hasText(request.getAuthorName()), Comment::getAuthorName, request.getAuthorName())
                .eq(request.getArticleId() != null, Comment::getArticleId, request.getArticleId())
                .ge(request.getCreateDateTimeStart() != null, Comment::getCreatedAt, request.getCreateDateTimeStart())
                .le(request.getCreateDateTimeEnd() != null, Comment::getCreatedAt, request.getCreateDateTimeEnd());

        if ("asc".equalsIgnoreCase(request.getLikeCountSort())) {
            wrapper.orderByAsc(Comment::getLikeCount);
        } else if ("desc".equalsIgnoreCase(request.getLikeCountSort())) {
            wrapper.orderByDesc(Comment::getLikeCount);
        }
        wrapper.orderByDesc(Comment::getCreatedAt);

        Page<Comment> page = commentMapper.selectPage(new Page<>(request.getPage(), request.getSize()), wrapper);

        var items = page.getRecords().stream()
                .map(c -> new CommentListResponse.CommentItem(
                        c.getId(), c.getArticleId(), c.getParentId(), c.getAuthorName(),
                        c.getAuthorEmail(), c.getContent(), c.getStatus(), c.getLikeCount(), c.getCreatedAt()))
                .toList();

        return new CommentListResponse(
                items,
                new CommentListResponse.Pagination(request.getPage(), request.getSize(), page.getTotal())
        );
    }

    @Override
    public void reviewComment(ReviewCommentRequest request) {
        Comment comment = commentMapper.selectById(request.getId());
        if (comment == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "评论不存在");
        }
        comment.setStatus(CommentStatus.valueOf(request.getStatus().toUpperCase()));
        commentMapper.updateById(comment);
    }

    @Override
    public void delComment(Integer id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "评论不存在");
        }
        comment.setStatus(CommentStatus.TRASH);
        commentMapper.updateById(comment);
    }

    @Override
    public void publishAuthorComment(PublishAuthorCommentRequest request, Integer userId, String name) {
        Article article = articleMapper.selectById(request.getArticleId());
        if (article == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "文章不存在");
        }
        AdminUser adminUser = adminUserMapper.selectById(userId);
        if (adminUser == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "用户不存在");
        }
        Comment comment = new Comment();
        comment.setArticleId(request.getArticleId());
        comment.setParentId(null);
        comment.setAuthorName(name);
        comment.setContent(request.getContent());
        comment.setStatus(CommentStatus.APPROVED);
        comment.setIsAuthor(true);
        comment.setAuthorEmail(adminUser.getEmail());
        comment.setAuthorPhone(adminUser.getPhone());
        commentMapper.insert(comment);
    }

    @Override
    public void replyComment(ReplyCommentRequest request, Integer userId, String name) {
        Comment parent = commentMapper.selectById(request.getId());
        if (parent == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "评论不存在");
        }
        AdminUser adminUser = adminUserMapper.selectById(userId);
        if (adminUser == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "用户不存在");
        }
        Comment comment = new Comment();
        comment.setArticleId(parent.getArticleId());
        comment.setParentId(parent.getId());
        comment.setAuthorName(name);
        comment.setContent(request.getContent());
        comment.setStatus(CommentStatus.APPROVED);
        comment.setIsAuthor(true);
        comment.setAuthorEmail(adminUser.getEmail());
        comment.setAuthorPhone(adminUser.getPhone());
        commentMapper.insert(comment);
    }
}
