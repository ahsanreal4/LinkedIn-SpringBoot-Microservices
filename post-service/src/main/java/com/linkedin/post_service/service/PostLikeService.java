package com.linkedin.post_service.service;

import com.linkedin.post_service.dto.likes.PostLikeDto;

import java.util.List;

public interface PostLikeService {
    String likePost(long postId, long userId);
    List<PostLikeDto> getPostLikes(long postId);
    int getNumberOfPostLikes(long postId);
    boolean isLikedByUser(long postId, long userId);
}
