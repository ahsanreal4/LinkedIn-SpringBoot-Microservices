package com.linkedin.post_service.service.impl;

import com.linkedin.post_service.dto.CreatePostDto;
import com.linkedin.post_service.dto.PostDto;
import com.linkedin.post_service.dto.comment.PostCommentDto;
import com.linkedin.post_service.entity.Post;
import com.linkedin.post_service.entity.user.User;
import com.linkedin.post_service.exception.ApiException;
import com.linkedin.post_service.repository.PostRepository;
import com.linkedin.post_service.service.CommentService;
import com.linkedin.post_service.service.PostLikeService;
import com.linkedin.post_service.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final String POST_DOES_NOT_EXIST = "post does not exist";
    private final String CANNOT_DELETE_OTHER_POST = "cannot delete someone else post";

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    public PostServiceImpl(PostRepository postRepository,
                           CommentService commentService,
                           PostLikeService postLikeService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.postLikeService = postLikeService;
    }

    @Override
    public PostDto createPost(CreatePostDto createPostDto, long userId) {
        Post post = new Post();
        post.setTitle(createPostDto.getTitle());
        post.setDescription(createPostDto.getDescription());
        post.setPostedAt(new Date());

        User user = new User();
        user.setId(userId);

        post.setPostedBy(user);

        Post savedPost = postRepository.save(post);

        return mapToDto(savedPost);
    }

    private PostDto getPostData(Post post, boolean ignoreFetchingComments, long userId) {
        PostDto postDto = mapToDto(post);

        long id = post.getId();

        if(!ignoreFetchingComments) {
            List<PostCommentDto> commentsDto = this.commentService.getPostComments(id);
            postDto.setComments(commentsDto);
            postDto.setNumComments(commentsDto.size());
        }
        else {
            int numComments = this.commentService.getNumberOfPostComments(id);
            postDto.setNumComments(numComments);
        }

        int numLikes = this.postLikeService.getNumberOfPostLikes(id);

        boolean isLiked = false;

        // Only check if likes on post are greater than 0
        if(numLikes > 0) isLiked = this.postLikeService.isLikedByUser(id, userId);

        postDto.setNumLikes(numLikes);
        postDto.setLiked(isLiked);

        return postDto;
    }

    @Override
    public PostDto getPostById(long id, long userId) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if(optionalPost.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, POST_DOES_NOT_EXIST);

        Post post = optionalPost.get();

        return getPostData(post, false, userId);
    }

    @Override
    public List<PostDto> getAllPosts(long userId) {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> getPostData(post, true, userId)).collect(Collectors.toList());
    }

    @Override
    public String deletePostById(long id, long userId, boolean isAdmin) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, POST_DOES_NOT_EXIST);

        Post post = optionalPost.get();

        if (!isAdmin && !post.getPostedBy().getId().equals(userId)) throw new ApiException(HttpStatus.BAD_REQUEST, CANNOT_DELETE_OTHER_POST);

        postRepository.delete(post);

        return "Post deleted successfully";
    }

    private PostDto mapToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setPostedAt(post.getPostedAt());
        postDto.setPostedBy(post.getPostedBy().getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());

        return postDto;
    }
}
