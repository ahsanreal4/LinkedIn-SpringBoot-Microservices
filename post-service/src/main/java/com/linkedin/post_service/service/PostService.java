package com.linkedin.post_service.service;

import com.linkedin.post_service.dto.CreatePostDto;
import com.linkedin.post_service.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(CreatePostDto createPostDto, long userId);
    PostDto getPostById(long id, long userId);
    List<PostDto> getAllPosts(long userId);
    String deletePostById(long id, long userId, boolean isAdmin);
}
