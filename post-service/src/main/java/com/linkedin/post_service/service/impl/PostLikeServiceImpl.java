package com.linkedin.post_service.service.impl;

import com.linkedin.post_service.dto.likes.PostLikeDto;
import com.linkedin.post_service.entity.Post;
import com.linkedin.post_service.entity.PostLikes;
import com.linkedin.post_service.entity.user.User;
import com.linkedin.post_service.exception.ApiException;
import com.linkedin.post_service.repository.PostLikesRepository;
import com.linkedin.post_service.repository.PostRepository;
import com.linkedin.post_service.service.PostLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostLikeServiceImpl implements PostLikeService {
    private final String POST_DOES_NOT_EXIST = "post does not exist";

    private final PostLikesRepository postLikesRepository;
    private final PostRepository postRepository;

    public PostLikeServiceImpl(PostLikesRepository postLikesRepository,
                               PostRepository postRepository) {
        this.postLikesRepository = postLikesRepository;
        this.postRepository = postRepository;
    }

    @Override
    public String likePost(long postId, long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, POST_DOES_NOT_EXIST);

        Post post = optionalPost.get();

        PostLikes prevPostLikes = postLikesRepository.findByUserIdAndPostId(userId, postId);

        if (prevPostLikes != null) {
            postLikesRepository.delete(prevPostLikes);
            return "Post Like removed";
        }

        User user = new User();
        user.setId(userId);

        PostLikes postLikes = new PostLikes();
        postLikes.setPost(post);
        postLikes.setUser(user);
        postLikesRepository.save(postLikes);
        return "Post Liked";
    }

    @Override
    public List<PostLikeDto> getPostLikes(long postId) {
        List<PostLikes> postLikes = postLikesRepository.findByPostId(postId);

        return postLikes.stream().map(this::mapToDto).toList();
    }

    @Override
    public int getNumberOfPostLikes(long postId) {
        return postLikesRepository.countByPostId(postId);
    }

    @Override
    public boolean isLikedByUser(long postId, long userId) {
        return postLikesRepository.existsByUserIdAndPostId(userId, postId);
    }

    private PostLikeDto mapToDto(PostLikes postLike) {
        PostLikeDto postLikeDto = new PostLikeDto();
        postLikeDto.setId(postLike.getId());
        postLikeDto.setPostId(postLike.getPost().getId());
        postLikeDto.setUserId(postLike.getUser().getId());

        return postLikeDto;
    }
}
