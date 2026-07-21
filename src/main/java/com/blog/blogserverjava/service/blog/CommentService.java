package com.blog.blogserverjava.service.blog;

import com.blog.blogserverjava.dto.comment.CommentListRequest;
import com.blog.blogserverjava.dto.comment.CommentListResponse;
import com.blog.blogserverjava.dto.comment.PublishAuthorCommentRequest;
import com.blog.blogserverjava.dto.comment.ReplyCommentRequest;
import com.blog.blogserverjava.dto.comment.ReviewCommentRequest;

public interface CommentService {
    CommentListResponse getCommentsList(CommentListRequest request);

    void reviewComment(ReviewCommentRequest request);

    void delComment(Integer id);

    void publishAuthorComment(PublishAuthorCommentRequest request, Integer userId, String name);

    void replyComment(ReplyCommentRequest request, Integer userId, String name);
}
