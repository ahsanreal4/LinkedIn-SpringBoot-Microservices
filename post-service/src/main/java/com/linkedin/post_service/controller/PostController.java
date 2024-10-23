package com.linkedin.post_service.controller;

import com.linkedin.post_service.dto.CreatePostDto;
import com.linkedin.post_service.dto.PostDto;
import com.linkedin.post_service.service.PostService;
import com.linkedin.post_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserUtils userUtils;

    public PostController(PostService postService, UserUtils userUtils) {
        this.postService = postService;
        this.userUtils = userUtils;
    }

    @PostMapping("")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostDto createPostDto, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        PostDto createdPost = this.postService.createPost(createPostDto, userId);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPostFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String fileType,
                                                 @RequestParam("postId") long postId, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        String response = this.postService.uploadPostFile(file, fileType, postId, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<List<PostDto>> getAllPosts(HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        List<PostDto> posts = this.postService.getAllPosts(userId);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        PostDto post = this.postService.getPostById(id, userId);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable("id") long id, HttpServletRequest request) {
        boolean isAdmin = userUtils.isAdmin(request);
        long userId = userUtils.getUserId(request);

        String response = this.postService.deletePostById(id, userId, isAdmin);

        return ResponseEntity.ok(response);
    }
}
