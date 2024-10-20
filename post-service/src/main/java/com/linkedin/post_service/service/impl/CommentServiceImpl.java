package com.linkedin.post_service.service.impl;

import com.linkedin.post_service.dto.comment.CreatePostCommentDto;
import com.linkedin.post_service.dto.comment.PostCommentDto;
import com.linkedin.post_service.entity.Post;
import com.linkedin.post_service.entity.PostComment;
import com.linkedin.post_service.entity.user.User;
import com.linkedin.post_service.exception.ApiException;
import com.linkedin.post_service.repository.PostCommentsRepository;
import com.linkedin.post_service.repository.PostRepository;
import com.linkedin.post_service.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final String POST_DOES_NOT_EXIST = "post does not exist";
    private final String PARENT_COMMENT_DOES_NOT_EXIST = "parent comment does not exist";
    private final String COMMENT_DOES_NOT_EXIST = "comment does not exist";
    private final String CANNOT_DELETE_OTHERS_COMMENT = "Deleting others comment is not allowed";

    private final PostCommentsRepository postCommentsRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(PostCommentsRepository postCommentsRepository,
                              PostRepository postRepository) {
        this.postCommentsRepository = postCommentsRepository;
        this.postRepository = postRepository;
    }

    @Override
    public PostCommentDto addPostComment(CreatePostCommentDto createPostCommentDto, long userId) {
        Optional<Post> optionalPost = postRepository.findById(createPostCommentDto.getPostId());

        if (optionalPost.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, POST_DOES_NOT_EXIST);

        Post post = optionalPost.get();

        User user = new User();
        user.setId(userId);

        PostComment postComment = new PostComment();
        postComment.setPostedAt(new Date());
        postComment.setPost(post);
        postComment.setUser(user);
        postComment.setText(createPostCommentDto.getText());

        Long parentId = createPostCommentDto.getParentId();

        if (parentId != null) {
            Optional<PostComment> optionalPostComment = postCommentsRepository.findById(parentId);

            if (optionalPostComment.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, PARENT_COMMENT_DOES_NOT_EXIST);

            PostComment parentComment = optionalPostComment.get();

            postComment.setParent(parentComment);
        };

        PostComment savedComment = postCommentsRepository.save(postComment);

        return mapToDto(savedComment);
    }

    @Override
    public List<PostCommentDto> getPostComments(long id) {
        List<PostComment> comments = postCommentsRepository.findByPostIdAndParentId(id, null);

        return comments.stream().map(this::mapToDto).toList();
    }

    @Override
    public int getNumberOfPostComments(long id) {
        return postCommentsRepository.countByPostId(id);
    }

    @Override
    public List<PostCommentDto> getReplyComments(long id) {
        List<PostComment> comments = postCommentsRepository.findByParentId(id);

        return comments.stream().map(this::mapToDto).toList();
    }

    @Override
    public PostCommentDto getCommentById(long id) {
        Optional<PostComment> optionalPostComment = postCommentsRepository.findById(id);

        if (optionalPostComment.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, COMMENT_DOES_NOT_EXIST);

        PostComment postComment = optionalPostComment.get();

        return mapToDto(postComment);
    }

    @Override
    public String deleteCommentById(long id, boolean isAdmin, long userId) {
        Optional<PostComment> optionalPostComment = postCommentsRepository.findById(id);

        if(optionalPostComment.isEmpty()) throw new ApiException((HttpStatus.NOT_FOUND), COMMENT_DOES_NOT_EXIST);

        PostComment postComment = optionalPostComment.get();

        if (!isAdmin && !postComment.getUser().getId().equals(userId)) throw new ApiException(HttpStatus.BAD_REQUEST, CANNOT_DELETE_OTHERS_COMMENT);

        postCommentsRepository.delete(postComment);

        return "Post Comment deleted successfully";
    }

    private PostCommentDto mapToDto(PostComment comment) {
        PostCommentDto dto = new PostCommentDto();
        dto.setId(comment.getId());
        dto.setUser(comment.getUser().getId());
        dto.setText(comment.getText());
        if(comment.getParent() != null) dto.setParent(comment.getParent().getId());
        dto.setPost(comment.getPost().getId());
        dto.setPostedAt(comment.getPostedAt());

        return dto;
    }

}
