package com.linkedin.post_service.controller;

import com.linkedin.post_service.dto.comment.CreatePostCommentDto;
import com.linkedin.post_service.dto.comment.PostCommentDto;
import com.linkedin.post_service.service.CommentService;
import com.linkedin.post_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserUtils userUtils;

    public CommentController(CommentService commentService, UserUtils userUtils) {
        this.commentService = commentService;
        this.userUtils = userUtils;
    }

    @PostMapping("")
    public ResponseEntity<PostCommentDto> addCommentToPost(@Valid @RequestBody CreatePostCommentDto createPostCommentDto, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto, userId);

        return new ResponseEntity<>(postCommentDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostCommentDto> getCommentById(@PathVariable("id") long id) {
        PostCommentDto postCommentDto = this.commentService.getCommentById(id);

        return ResponseEntity.ok(postCommentDto);
    }

    @GetMapping("/replies/{id}")
    public ResponseEntity<List<PostCommentDto>> getReplyComments(@PathVariable("id") long id) {
        List<PostCommentDto> postCommentDtos = this.commentService.getReplyComments(id);

        return ResponseEntity.ok(postCommentDtos);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<PostCommentDto>> getPostComments(@PathVariable("id") long id) {
        List<PostCommentDto> postCommentDtos = this.commentService.getPostComments(id);

        return ResponseEntity.ok(postCommentDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("id") long id, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);
        boolean isAdmin = userUtils.isAdmin(request);

        String response = this.commentService.deleteCommentById(id, isAdmin, userId);

        return ResponseEntity.ok(response);
    }
}
