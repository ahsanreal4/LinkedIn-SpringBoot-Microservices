package com.linkedin.post_service.service;

import com.linkedin.post_service.dto.comment.CreatePostCommentDto;
import com.linkedin.post_service.dto.comment.PostCommentDto;

import java.util.List;

public interface CommentService {
    PostCommentDto addPostComment(CreatePostCommentDto createPostCommentDto, long userId);
    List<PostCommentDto> getPostComments(long id);
    int getNumberOfPostComments(long id);
    List<PostCommentDto> getReplyComments(long id);
    PostCommentDto getCommentById(long id);
    String deleteCommentById(long id, boolean isAdmin, long userId);
}
