package com.linkedin.post_service.controller;

import com.linkedin.post_service.dto.likes.CreateLikeDto;
import com.linkedin.post_service.service.PostLikeService;
import com.linkedin.post_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final UserUtils userUtils;

    public PostLikeController(PostLikeService postLikeService, UserUtils userUtils) {
        this.postLikeService = postLikeService;
        this.userUtils = userUtils;
    }


    @PostMapping("")
    public ResponseEntity<String> likePost(@Valid @RequestBody CreateLikeDto createLikeDto, HttpServletRequest request) {
        long userId = Long.parseLong(userUtils.getUserId(request));

        String response = this.postLikeService.likePost(createLikeDto.getPostId(), userId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
