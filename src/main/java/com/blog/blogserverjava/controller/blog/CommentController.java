package com.blog.blogserverjava.controller.blog;

import com.blog.blogserverjava.common.Result;
import com.blog.blogserverjava.dto.comment.*;
import com.blog.blogserverjava.service.blog.CommentService;
import com.blog.blogserverjava.utils.JwtPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/getCommentsList")
    public Result<CommentListResponse> getCommentsList(CommentListRequest request) {
        return Result.success(commentService.getCommentsList(request));
    }

    @PutMapping("/reviewComment")
    public Result<Void> reviewComment(@Valid @RequestBody ReviewCommentRequest request) {
        commentService.reviewComment(request);
        return Result.success(null);
    }

    @DeleteMapping("/delComment")
    public Result<Void> delComment(@RequestParam Integer id) {
        commentService.delComment(id);
        return Result.success(null);
    }

    @PostMapping("/publishAuthorComment")
    public Result<Void> publishAuthorComment(@Valid @RequestBody PublishAuthorCommentRequest request, HttpServletRequest httpRequest) {
        JwtPayload user = (JwtPayload) httpRequest.getAttribute("user");
        commentService.publishAuthorComment(request, user.id(), user.name());
        return Result.success(null);
    }

    @PostMapping("/replyComment")
    public Result<Void> replyComment(@Valid @RequestBody ReplyCommentRequest request, HttpServletRequest httpRequest) {
        JwtPayload user = (JwtPayload) httpRequest.getAttribute("user");
        commentService.replyComment(request, user.id(), user.name());
        return Result.success(null);
    }
}
