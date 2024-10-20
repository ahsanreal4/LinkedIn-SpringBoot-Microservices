package com.linkedin.post_service.repository;

import com.linkedin.post_service.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
    PostLikes findByUserIdAndPostId(long userId, long postId);
    int countByPostId(long postId);
    boolean existsByUserIdAndPostId(long userId, long postId);
    List<PostLikes> findByPostId(long postId);
}
