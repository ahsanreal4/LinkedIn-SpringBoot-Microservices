package com.linkedin.post_service.service.impl;

import com.linkedin.post_service.dto.CreatePostDto;
import com.linkedin.post_service.dto.PostDto;
import com.linkedin.post_service.dto.comment.PostCommentDto;
import com.linkedin.post_service.entity.Post;
import com.linkedin.post_service.entity.PostFiles;
import com.linkedin.post_service.entity.user.User;
import com.linkedin.post_service.enums.PostFileType;
import com.linkedin.post_service.exception.ApiException;
import com.linkedin.post_service.feign_clients.impl.FileServiceClientImpl;
import com.linkedin.post_service.repository.PostFilesRepository;
import com.linkedin.post_service.repository.PostRepository;
import com.linkedin.post_service.service.CommentService;
import com.linkedin.post_service.service.PostLikeService;
import com.linkedin.post_service.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final String POST_DOES_NOT_EXIST = "post does not exist";
    private final String CANNOT_DELETE_OTHER_POST = "cannot delete someone else post";

    private final PostRepository postRepository;
    private final PostFilesRepository postFilesRepository;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    private final FileServiceClientImpl fileService;

    public PostServiceImpl(PostRepository postRepository, PostFilesRepository postFilesRepository,
                           CommentService commentService,
                           PostLikeService postLikeService, FileServiceClientImpl fileService) {
        this.postRepository = postRepository;
        this.postFilesRepository = postFilesRepository;
        this.commentService = commentService;
        this.postLikeService = postLikeService;
        this.fileService = fileService;
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

        PostFiles postFiles = post.getPostFile();
        if (postFiles != null) postDto.setLink(postFiles.getLink());

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

        String fileUrl = null;

        Optional<PostFiles> postFiles = postFilesRepository.findByPostId(post.getId());
        if (postFiles.isPresent()) fileUrl = postFiles.get().getLink();

        postRepository.delete(post);

        if (fileUrl == null) return "Post deleted successfully";

        try {
            String[] urlSplit = fileUrl.split("/");

            String url = null;

            if (urlSplit.length > 1){
                String[] urlSplit2 = urlSplit[urlSplit.length - 1].split("\\.");

                if(urlSplit2.length == 0) url = urlSplit[urlSplit.length - 1];
                else url = urlSplit2[0];
            }

            if(url != null) fileService.deleteFileById(url);
            else System.out.println("Invalid file url => " + fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while deleting the post's file");
        }

        return "Post deleted successfully";
    }

    @Override
    public String uploadPostFile(MultipartFile file, String fileType, long postId, long userId) {
        PostFileType postFileType;

        try {
            postFileType = PostFileType.valueOf(fileType.toUpperCase());
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid File Type. Only " + PostFileType.IMAGE + ", " + PostFileType.VIDEO + " are supported");
        }

        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, POST_DOES_NOT_EXIST);
        else if(!optionalPost.get().getPostedBy().getId().equals(userId)) throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot upload file for someone else's post");

        String url;

        try {
            url = fileService.uploadFile(file);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading file");
        }


        Post post = optionalPost.get();
        PostFiles postFiles = new PostFiles();
        postFiles.setPost(post);
        postFiles.setType(postFileType);
        postFiles.setLink(url);

        postFilesRepository.save(postFiles);

        return url;
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
